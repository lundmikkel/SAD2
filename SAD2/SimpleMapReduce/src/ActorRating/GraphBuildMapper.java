package ActorRating;

import engine.Collector;
import engine.Mapper;
import engine.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GraphBuildMapper implements Mapper<String, String, Integer, Tuple<Float, Integer>> {

    @Override
    public void map(String fileName, String fileContent, Collector<Integer, Tuple<Float, Integer>> collector) {
        StringTokenizer tokenizer = new StringTokenizer(fileContent);

        //Parse id and rating
        //int movieID = Integer.parseInt(tokenizer.nextToken());
        tokenizer.nextToken(); //skip movie ID

        float movieRating = Float.parseFloat(tokenizer.nextToken()) / 10;

        //Parse all actors
        List<Integer> actors = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            actors.add(Integer.parseInt(tokenizer.nextToken()));
        }

        //Generate graph
        for (int actorA : actors){
            actors.stream().filter(actorB -> actorA != actorB).forEach(actorB -> {
                collector.collect(actorA, new Tuple<>(movieRating, actorB));
            });
        }
    }
}
