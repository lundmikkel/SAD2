package actorrating;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * Created by Rasmus on 03-12-2014.
 */
public class EntireFileInputFormat extends FileInputFormat<Text, Text> {
    @Override
    public RecordReader<Text, Text> getRecordReader(InputSplit split, JobConf job, Reporter reporter) throws IOException {
        reporter.setStatus(split.toString());
        return new SequenceFileAsTextRecordReader(job, (FileSplit) split);
    }

    @Override
    protected boolean isSplitable(FileSystem fs, Path path){
        return false;
    }
}
