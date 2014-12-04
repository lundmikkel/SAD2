package example;

import engine.*;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class WordCountReducer implements Reducer<String, Integer, String, Integer> {
    @Override
    public void reduce(String s, Iterable<Integer> values, Collector<String, Integer> collector) {
        int sum = 0;
        for (int v : values)
            sum += v;
        collector.collect(s, sum);
    }
}
