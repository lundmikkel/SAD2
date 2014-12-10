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
        StringTokenizer tokenizer = new StringTokenizer(fileContent, "\r\n");
        while (tokenizer.hasMoreTokens()){
            String line = tokenizer.nextToken();
            System.out.println("Reading line: " + line);
            StringTokenizer lineTokenizer = new StringTokenizer(line, ",");
            float movieRating = Float.parseFloat(lineTokenizer.nextToken()) / 10;
            //Parse all actors
            List<Integer> actors = new ArrayList<>();
            while (lineTokenizer.hasMoreTokens()){
                actors.add(Integer.parseInt(lineTokenizer.nextToken()));
            }
            //Generate graph
            for (int actorA : actors){
                actors.stream().filter(actorB -> actorA != actorB).forEach(actorB -> {
                    collector.collect(actorA, new Tuple<>(movieRating, actorB));
                });
            }
        }
    }
}
