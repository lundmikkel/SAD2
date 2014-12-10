package ImdbActorRank;

import engine.Collector;
import engine.Mapper;
import engine.Tuple;
import imdb.Actor;

import java.util.List;

public class ActorRankMapper implements Mapper<Actor, List<Tuple<Actor, Double>>, Actor, Tuple<Actor, Double>> {

    @Override
    public void map(Actor actor, List<Tuple<Actor, Double>> tuples, Collector<Actor, Tuple<Actor, Double>> collector) {
        double alpha = 0.2;
        int sum = 0;

        for (Tuple<Actor, Double> tuple : tuples)
            sum += tuple.value;

        double rank = alpha / Actor.count() + (1.0 - alpha) * sum / tuples.size();

        for (Tuple<Actor, Double> tuple : tuples)
            collector.collect(tuple.key, new Tuple<>(actor, rank));
    }
}
