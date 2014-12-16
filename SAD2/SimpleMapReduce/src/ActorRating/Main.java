package ActorRating;

import WordCount.FileLoaderMapper;
import engine.Executor;
import engine.ReduceSkipper;
import engine.Tuple;
import imdb.Actor;
import imdb.ImdbParser;
import imdb.ImdbParser.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        List<Tuple<String, String>> files = new ArrayList<>();
        scanDir("data/ActorRating/").forEach((s) -> files.add(new Tuple<>(s,"")));

        int load = ImdbParser.Table.getLoad(Table.ACTORS, Table.MOVIES);
        ImdbParser.Parse("data/imdb-r.txt", load);

        float dampingFactor = 0.85f;
        int actorCount = 460428;
        List<Tuple<Integer, Float>> result = new Executor(files)
                .add(new FileLoaderMapper(), new ReduceSkipper())
                .add(new GraphBuildMapper(), new GraphBuildReducer())
                .add(new TransformMapper(), new ReduceSkipper(false))
                .add(new ActorRatingMapper(dampingFactor, actorCount), new ReduceSkipper(false), 5)
                .add(new ActorRatingOutputMapper(), new ActorRatingOutputReducer())
                .execute();

        Collections.sort(result, (a1, a2) -> (int)Math.signum(a2.value - a1.value));
        PrintWriter pw = new PrintWriter(new File("data/MRoutput.txt"));
        result.forEach(pw::println);
        pw.flush();
        pw.close();

        result.stream().limit(100).forEach(a -> System.out.println(Actor.get(a.key)+": "+a.value));
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

