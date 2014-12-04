package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * An executor that will execute a series of mapper/reducer pairs in parallel.
 * @param <InKey> The input keys' type
 * @param <InValue> The input values' type
 * @param <OutKey> The output keys' type
 * @param <OutValue> The output values' type
 */
public class Executor<InKey, InValue, OutKey, OutValue> {

    List<Tuple<Mapper, Reducer>> rounds = new ArrayList<>();
    List<Tuple<InKey, InValue>> input;

    /**
     * Construct an executor
     * @param input List of tuples that is given to the first mapper
     */
    public Executor(List<Tuple<InKey, InValue>> input){
        this.input = input;
    }

    /**
     * Add a mapper and reducer pair to the executor.
     * Mappers and reducers will be executed in the order they were added,
     * and must accept the previous output in the chain as input.
     * @param mapper A mapper
     * @param reducer A reducer
     * @return The executor to allow chaining
     */
    public Executor add(Mapper mapper, Reducer reducer){
        rounds.add(new Tuple<>(mapper, reducer));

        return this;
    }

    /**
     * Adds the mapper and reducer pair multiple times.
     * @param mapper A mapper
     * @param reducer A reducer
     * @return The executor to allow chaining
     */
    public Executor add(Mapper mapper, Reducer reducer, int repetitions){
        for (/* */; repetitions >= 0; --repetitions)
            add(mapper, reducer);

        return this;
    }

    /**
     * Execute all the given mapper/reducer pairs in given order and return the output from the last reducer.
     * @return Output from last reducer
     * @throws Exception
     */
    public List<Tuple<OutKey, OutValue>> execute() throws Exception{
        // Preprocess input
        List<Tuple<?,?>> data = new ArrayList<>(input.size());
        data.addAll(input);

        // Execute all rounds
        for (Tuple<Mapper, Reducer> round : rounds){
            final Mapper mapper = round.key;
            final Reducer reducer = round.value;

            // Spawn mappers
            ExecutorService mappersExecutor = Executors.newWorkStealingPool();
            final MapCollector mapCollector = new MapCollector();

            for (Tuple<?, ?> dataItem : data){
                final Tuple<?, ?> d = dataItem;
                mappersExecutor.execute(() -> mapper.map(d.key, d.value, mapCollector));
            }

            // Synchronize
            mappersExecutor.shutdown();
            mappersExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS.NANOSECONDS);

            // Spawn reducers
            ExecutorService reducersExecutor = Executors.newWorkStealingPool();
            final ReduceCollector reduceCollector = new ReduceCollector();

            for (Object key : mapCollector.map.keySet()){
                final Object k = key;
                final Iterable vs = (Iterable)mapCollector.map.get(key);
                reducersExecutor.execute(() -> reducer.reduce(k, vs, reduceCollector));
            }

            // Synchronize
            reducersExecutor.shutdown();
            reducersExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS.NANOSECONDS);


            // Collect reducers result
            data = reduceCollector.list;
        }

        // Preprocess output
        List<Tuple<OutKey, OutValue>> result = new ArrayList<>();
        for (Tuple<?, ?> d : data)
            result.add((Tuple<OutKey, OutValue>) d);

        return result;
    }

}