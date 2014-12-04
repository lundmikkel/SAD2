package example;

import engine.Collector;
import engine.Mapper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class FileLoaderMapper implements Mapper<String, String, String, String> {
    @Override
    public void map(String filename, String dummy, Collector<String, String> collector) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        collector.collect(filename, content);
    }
}
