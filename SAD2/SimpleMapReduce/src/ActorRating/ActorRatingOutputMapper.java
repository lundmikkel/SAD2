package ActorRating;

import engine.Collector;
import engine.Mapper;
import engine.Triple;

public class ActorRatingOutputMapper implements Mapper<Integer, Iterable<Triple<Float, Integer, Float>>, Integer, Float> {
    @Override
    public void map(Integer actorA, Iterable<Triple<Float, Integer, Float>> connections, Collector<Integer, Float> collector) {
        for (Triple<Float, Integer, Float> connection : connections) {
            collector.collect(connection.item2, connection.item3);
        }
    }
}
