package WordCount;

import engine.Collector;
import engine.Reducer;

public class EchoReducer implements Reducer {
    private final boolean unRoll;
    public EchoReducer(){
        unRoll = true;
    }
    public EchoReducer(boolean unRoll){
        this.unRoll = unRoll;
    }

    @Override
    public void reduce(Object k, Iterable iterable, Collector collector) {
        if (!unRoll)
            collector.collect(k,iterable);
        else
            for (Object v : iterable) {
                collector.collect(k, v);
            }
    }
}