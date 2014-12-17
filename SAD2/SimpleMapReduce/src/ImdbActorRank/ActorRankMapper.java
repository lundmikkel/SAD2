package ImdbActorRank;

import engine.Collector;
import engine.Mapper;
import engine.Tuple;
import imdb.Actor;

import java.util.List;

public class ActorRankMapper implements Mapper<Actor, List<Tuple<Actor, Double>>, Actor, Tuple<Actor, Double>> {
    final float dampingFactor;

    public ActorRankMapper(float dampingFactor) {
        this.dampingFactor = dampingFactor;
    }

    @Override
    public void map(Actor actor, List<Tuple<Actor, Double>> tuples, Collector<Actor, Tuple<Actor, Double>> collector) {
        double dampingFactor = 0.85d;
        int sum = 0;

        for (Tuple<Actor, Double> tuple : tuples)
            sum += tuple.value;

        double rank = (1 - dampingFactor) / Actor.count() + dampingFactor * sum / tuples.size();

        for (Tuple<Actor, Double> tuple : tuples)
            collector.collect(tuple.key, new Tuple<>(actor, rank));
    }
}
