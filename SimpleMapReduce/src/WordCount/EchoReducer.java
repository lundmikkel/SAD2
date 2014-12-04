package WordCount;

import engine.Collector;
import engine.Reducer;

public class EchoReducer implements Reducer {
    @Override
    public void reduce(Object k, Iterable iterable, Collector collector) {
        for (Object v : iterable){
            collector.collect(k,v);
        }
    }
}