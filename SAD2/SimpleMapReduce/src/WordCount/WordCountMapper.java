package WordCount;

import engine.*;

import java.util.StringTokenizer;

public class WordCountMapper implements Mapper<String, String, String, Integer> {
    @Override
    public void map(String filename, String fileContent, Collector<String, Integer> collector) {
        StringTokenizer tokenizer = new StringTokenizer(fileContent);
        while (tokenizer.hasMoreTokens())
            collector.collect(tokenizer.nextToken(), 1);
    }
}
