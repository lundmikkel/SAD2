package actorrating;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Rasmus on 03-12-2014.
 */
public class InitialRound1 {

    public static class Map extends MapReduceBase implements Mapper<Text, Text, Integer, Tuple2<Double, Integer>> {
        @Override
        public void map(Text key, Text value, OutputCollector<Integer, Tuple2<Double, Integer>> output, Reporter reporter) throws IOException {
            String file = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(file);
            //Find movie id and rating
            int movieId = Integer.parseInt(tokenizer.nextToken());
            double movieRating = Double.parseDouble(tokenizer.nextToken());

            //collect actors
            List<Integer> actors = new ArrayList<Integer>();
            while (tokenizer.hasMoreTokens()){
                actors.add(Integer.parseInt(tokenizer.nextToken()));
            }

            //Build graph
            for (int actorA : actors){
                for (int actorB : actors){
                    if (actorA != actorB){
                        output.collect(actorA, new Tuple2<Double, Integer>(movieRating, actorB));
                    }
                }
            }
        }
    }
/*
    public static class Reduce extends MapReduceBase implements Reducer<Integer, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, List<Integer>> {
        @Override
        public void reduce(Integer key, Iterator<Tuple2<Integer, Integer>> values, OutputCollector<Tuple2<Integer, Integer>, List<Integer>> output, Reporter reporter) throws IOException {

        }
    }
*/
    public static class Reduce extends MapReduceBase implements Reducer<Integer, Tuple2<Double, Integer>, Text, IntWritable> {
        @Override
        public void reduce(Integer key, Iterator<Tuple2<Double, Integer>> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            //Calculate average
            double sum = 0;
            int count = 0;
            while (values.hasNext()) {
                sum += values.next().first();
                count++;
            }
            double startActorRating = sum/count;

            output.collect(new Text("Actor: " + key), new IntWritable((int)startActorRating));
        }
    }
}
