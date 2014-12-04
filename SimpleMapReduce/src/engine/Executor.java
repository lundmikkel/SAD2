package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class Executor<InKey, InValue, OutKey, OutValue> {

    List<Tuple<Mapper, Reducer>> rounds = new ArrayList<Tuple<Mapper, Reducer>>();
    List<Tuple<InKey, InValue>> input;

    public Executor(List<Tuple<InKey, InValue>> input){
        this.input = input;
    }

    public Executor add(Mapper mapper, Reducer reducer){
        rounds.add(new Tuple<Mapper, Reducer>(mapper, reducer));
        return this;
    }
    public Executor add(Mapper mapper, Reducer reducer, int repetitions){
        for (;repetitions >= 0; --repetitions)
            add(mapper,reducer);
        return this;
    }

    public List<Tuple<OutKey, OutValue>> execute() throws Exception{
        //List<Tuple<Object, Object>> data = (List<Tuple<Object, Object>>) input;

        List<Tuple<Object, Object>> data = new ArrayList<Tuple<Object, Object>>();
        for (Tuple<InKey, InValue> in : input)
            data.add((Tuple<Object, Object>)in);

        for (Tuple<Mapper, Reducer> round : rounds){
            final Mapper mapper = round.key;
            final Reducer reducer = round.value;

            //////////////////////////////////////////////////
            //Spawn mappers
            ExecutorService mappersExecutor = Executors.newWorkStealingPool();
            final MapCollector mapCollector = new MapCollector();

            for (Tuple<Object, Object> dataItem : data){
                final Tuple<Object, Object> d = dataItem;
                mappersExecutor.execute(()->{
                    try {
                        mapper.map(d.key, d.value, mapCollector);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            //Synchronize
            mappersExecutor.shutdown();
            mappersExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS.NANOSECONDS);


            //////////////////////////////////////////////////
            //Spawn reducers
            ExecutorService reducersExecutor = Executors.newWorkStealingPool();
            final ReduceCollector reduceCollector = new ReduceCollector();

            for (Object key : mapCollector.map.keySet()){
                final Object k = key;
                final Iterable vs = (Iterable)mapCollector.map.get(key);
                reducersExecutor.execute(() -> {
                    try {
                        reducer.reduce(k, vs, reduceCollector);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            //Synchronize
            reducersExecutor.shutdown();
            reducersExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS.NANOSECONDS);

            //Collect reducers result
            data = reduceCollector.list;
        }

        List<Tuple<OutKey, OutValue>> result = new ArrayList<>();
        for (Tuple<Object, Object> d : data)
            result.add((Tuple<OutKey, OutValue>) d);

        return result;
    }

}
