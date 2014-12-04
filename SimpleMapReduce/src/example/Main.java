package example;

import engine.Executor;
import engine.Tuple;
import example.WordCountMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        List<Tuple<String, String>> files = new ArrayList<>();
        //files.add(new Tuple<String, String>("hamlet.txt",""));
        //files.add(new Tuple<String, String>("hamlet.txt",""));
        files.add(new Tuple<String, String>("big.txt",""));

        long start = System.nanoTime();
        List<Tuple<String, Integer>> result = new Executor(files)
                .add(new FileLoaderMapper(), new EchoReducer())
                .add(new WordCountMapper(), new WordCountReducer())
                .execute();
        System.out.println("Time: " + (System.nanoTime() - start) / 1_000_000 + "ms");
        Collections.sort(result, (t1, t2) -> t2.value - t1.value);

        System.out.println("Results: ");
        for (Tuple<String, Integer> r : result){
            System.out.println(r.key + " : " + r.value);
            break;
        }
    }
}
