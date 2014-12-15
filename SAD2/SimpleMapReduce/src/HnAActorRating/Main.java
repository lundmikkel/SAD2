package HnAActorRating;

import WordCount.FileLoaderMapper;
import engine.Executor;
import engine.ReduceSkipper;
import engine.Tuple;
import imdb.Actor;
import imdb.ImdbParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        int k = 50;

        List<Tuple<String, String>> files = new ArrayList<>();
        scanDir("data/ActorRating/").forEach((s) -> files.add(new Tuple<>(s,"")));


        ImdbParser.Parse("data/imdb-r.txt", ImdbParser.Table.getLoad(ImdbParser.Table.ACTORS));

        Executor executor = new Executor(files)
                .add(new FileLoaderMapper(), new ReduceSkipper())
                .add(new GraphBuildMapper(), new ReduceSkipper(false));
        for (int i = 0; i < k; i++) {

            executor = executor.add(new HubsAuthActorRatingRound1Mapper(), new HubsAuthActorRatingRound1Reducer())
                    .add(new HubsAuthActorRatingRound2Mapper(), new HubsAuthActorRatingRound2Reducer());

        }
        List<Tuple<ActorMovie, Iterable<ActorMovie>>> result = executor.execute();

        Collections.sort(result, (a1, a2) -> (int)Math.signum(a2.key.value - a1.key.value));

        /*PrintWriter pw = new PrintWriter(new File("data/MRoutput.txt"));
        result.forEach(pw::println);
        pw.flush();
        pw.close();*/

        result.stream().filter(am -> am.key.key.charAt(0) == 'A').limit(100).forEach(a -> System.out.println(a.key.value + "\t" + Actor.get(Integer.parseInt(a.key.key.substring(1)))));
    }

    private static List<String> scanDir(String dir){
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++)
            if (listOfFiles[i].isFile())
                result.add(dir + listOfFiles[i].getName());
        return result;
    }
}

