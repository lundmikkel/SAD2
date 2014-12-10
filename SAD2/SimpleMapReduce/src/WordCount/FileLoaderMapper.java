package WordCount;

import engine.Collector;
import engine.Mapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLoaderMapper implements Mapper<String, String, String, String> {
    @Override
    public void map(String filename, String dummy, Collector<String, String> collector) {
        try {
            collector.collect(filename, new String(Files.readAllBytes(Paths.get(filename))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
