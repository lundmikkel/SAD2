package HnAActorRating;

import engine.Collector;
import engine.Mapper;
import engine.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GraphBuildMapper implements Mapper<String, String, ActorMovie, ActorMovie> {

    @Override
    public void map(String fileName, String fileContent, Collector<ActorMovie, ActorMovie> collector) {
        StringTokenizer tokenizer = new StringTokenizer(fileContent, "\r\n");
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            StringTokenizer lineTokenizer = new StringTokenizer(line, ",");
            String movieId = "M"+ Integer.parseInt(lineTokenizer.nextToken());
            float movieRating = Float.parseFloat(lineTokenizer.nextToken()) / 10;
            //Parse all actors
            List<String> actors = new ArrayList<>();
            while (lineTokenizer.hasMoreTokens()) {
                actors.add("A"+Integer.parseInt(lineTokenizer.nextToken()));
            }
            //Generate graph
            ActorMovie movieTuple = new ActorMovie(movieId, 1f);
            for (String a : actors) {
                ActorMovie actorTuple = new ActorMovie(a, 1f);
                collector.collect(movieTuple, actorTuple);
                collector.collect(actorTuple, movieTuple);
            }
        }
    }
}
