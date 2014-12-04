package WordCount;

import engine.*;

public class WordCountReducer implements Reducer<String, Integer, String, Integer> {
    @Override
    public void reduce(String s, Iterable<Integer> values, Collector<String, Integer> collector) {
        int sum = 0;
        for (int v : values)
            sum += v;
        collector.collect(s, sum);
    }
}