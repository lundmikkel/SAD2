package ActorRating;

import engine.Collector;
import engine.Reducer;
import engine.Tuple;

public class GraphBuildReducer implements Reducer<Integer, Tuple<Float, Integer>, Tuple<Integer, Float>, Iterable<Tuple<Float, Integer>>> {
    @Override
    public void reduce(Integer actorA, Iterable<Tuple<Float, Integer>> list, Collector<Tuple<Integer, Float>, Iterable<Tuple<Float, Integer>>> collector) {
        float sum = 0.0f;
        int count = 0;
        for (Tuple<Float, Integer> t : list) {
            sum += t.key;
            count++;
        }
        collector.collect(new Tuple<>(actorA, sum / count), list);
    }
}
