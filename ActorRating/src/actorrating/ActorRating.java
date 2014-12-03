package actorrating;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 * Created by Rasmus on 03-12-2014.
 */
public class ActorRating {

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(InitialRound1.class);
        conf.setJobName("actor rating initial round 1");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(InitialRound1.Map.class);
        conf.setCombinerClass(InitialRound1.Reduce.class);
        conf.setReducerClass(InitialRound1.Reduce.class);

        conf.setInputFormat(EntireFileInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));



        JobClient.runJob(conf);
    }

}
