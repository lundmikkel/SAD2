package ImdbActorRank;

import engine.Collector;
import engine.Mapper;
import engine.Tuple;
import imdb.Actor;
import imdb.Movie;

import java.util.Set;

public class EdgeMapper implements Mapper<Movie, Set<Actor>, Actor, Tuple<Actor, Double>> {

    @Override
    public void map(Movie movie, Set<Actor> actorSet, Collector<Actor, Tuple<Actor, Double>> collector) {
        double initialRank = 1.0 / Actor.count();
        Actor[] actors = actorSet.toArray(new Actor[1]);
        int count = actors.length;

        for (int i = 0; i < count; ++i)
            for (int j = 0; j < count; ++j)
                if (i != j)
                    collector.collect(actors[i], new Tuple<>(actors[j], initialRank));
    }
}