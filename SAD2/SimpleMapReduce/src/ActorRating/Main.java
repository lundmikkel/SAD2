package ActorRating;

import WordCount.FileLoaderMapper;
import engine.Executor;
import engine.ReduceSkipper;
import engine.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Tuple<String, String>> files = new ArrayList<>();
        scanDir("data/ActorRating/").forEach((s) -> files.add(new Tuple<>(s, "")));

        List<Tuple<Integer, Float>> result = new Executor(files)
                .add(new FileLoaderMapper(), new ReduceSkipper())
                .add(new GraphBuildMapper(), new GraphBuildReducer())
                .add(new TransformMapper(), new ReduceSkipper(false))
                .add(new ActorRatingMapper(), new ReduceSkipper(false), 10)
                .add(new ActorRatingOutputMapper(), new ActorRatingOutputReducer()).execute();

        Collections.sort(result, (a1, a2) -> (int) Math.signum(a1.value - a2.value));
        result.forEach(System.out::println);
    }

    private static List<String> scanDir(String dir) {
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++)
            if (listOfFiles[i].isFile())
                result.add(dir + listOfFiles[i].getName());
        return result;
    }
}

