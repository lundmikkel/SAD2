package ImdbActorRank;

import engine.Executor;
import engine.ReduceSkipper;
import engine.Tuple;
import imdb.Actor;
import imdb.ImdbParser;
import imdb.Movie;

import java.util.*;

public class ImdbActorRank {
    public static void main(String[] args) throws Exception {
        ImdbParser.Parse("data/imdb-r.txt", ImdbParser.Table.ACTORS);

        // Parse movies to list of tuples of movie -> actor list
        Collection<Movie> movies = Movie.getAll();
        List<Tuple<Movie, Set<Actor>>> input = new ArrayList<>(movies.size());
        for (Movie movie : movies) {
            Set<Actor> actors = new HashSet<>();
            movie.getRoles().forEach(r -> actors.add(r.getActor()));
            input.add(new Tuple<>(movie, actors));
        }

        float dampingFactor = 0.85f;
        List<Tuple<Actor, Tuple<Actor, Double>>> ranks = new Executor(input)
                .add(new EdgeMapper(), new ReduceSkipper())
                .add(new ActorRankMapper(dampingFactor), new ReduceSkipper())
                .execute();

        System.out.println("Result: " + ranks.size());
    }
}
