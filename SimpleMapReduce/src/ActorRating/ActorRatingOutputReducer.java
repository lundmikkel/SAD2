package ActorRating;

import engine.Collector;
import engine.Reducer;

public class ActorRatingOutputReducer implements Reducer<Integer, Float, Integer, Float> {
    @Override
    public void reduce(Integer actor, Iterable<Float> ratings, Collector<Integer, Float> collector) {
        collector.collect(actor, ratings.iterator().next());
    }
}
