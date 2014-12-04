package example;

import engine.*;

import java.util.StringTokenizer;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class WordCountMapper implements Mapper<String, String, String, Integer> {

    @Override
    public void map(String filename, String fileContent, Collector<String, Integer> collector) {
        StringTokenizer tokenizer = new StringTokenizer(fileContent);
        while (tokenizer.hasMoreTokens())
        {
            collector.collect(tokenizer.nextToken(), 1);
        }
    }
}
