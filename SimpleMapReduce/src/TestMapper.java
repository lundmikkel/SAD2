import engine.Collector;
import engine.Mapper;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class TestMapper implements Mapper<Integer, String, Integer, String> {

    @Override
    public void map(Integer integer, String s, Collector<Integer, String> collector) {

        for (int i = 0;i < integer;i++)
            collector.collect(integer, s);
    }
}
