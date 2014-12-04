package example;

import engine.Collector;
import engine.Reducer;

import java.util.Objects;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class EchoReducer implements Reducer {
    @Override
    public void reduce(Object k, Iterable iterable, Collector collector) throws Exception {
        for (Object v : iterable){
            collector.collect(k,v);
        }
    }
}
