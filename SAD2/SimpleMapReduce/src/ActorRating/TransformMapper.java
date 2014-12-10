package ActorRating;

import engine.Collector;
import engine.Mapper;
import engine.Triple;
import engine.Tuple;

public class TransformMapper implements Mapper<Tuple<Integer, Float>,Iterable<Tuple<Float, Integer>>,Integer, Triple<Float, Integer, Float>> {
    @Override
    public void map(Tuple<Integer, Float> actorInfo, Iterable<Tuple<Float, Integer>> relations, Collector<Integer, Triple<Float, Integer, Float>> collector) {
        for (Tuple<Float, Integer> relation : relations){
            collector.collect(relation.value, new Triple<>(relation.key, actorInfo.key, actorInfo.value));
        }
    }
}
