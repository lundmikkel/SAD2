package WordCount;

import engine.ReduceSkipper;
import engine.Executor;
import engine.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        List<Tuple<String, String>> files = new ArrayList<>();
        //files.add(new Tuple<>("data/hamlet.txt",""));
        //files.add(new Tuple<>("data/hamlet.txt",""));
        files.add(new Tuple<>("data/big.txt",""));

        long start = System.nanoTime();
        List<Tuple<String, Integer>> result = new Executor<String, String, String, Integer>(files)
                .add(new FileLoaderMapper(), new ReduceSkipper())
                .add(new WordCountMapper(), new WordCountReducer())
                .execute();
        System.out.println("Time: " + (System.nanoTime() - start) / 1_000_000 + "ms");

        Collections.sort(result, (t1, t2) -> t2.value - t1.value);

        System.out.println("Results: ");
        for (Tuple<String, Integer> tuple : result){
            System.out.println(tuple.key + " : " + tuple.value);
        }
    }
}
