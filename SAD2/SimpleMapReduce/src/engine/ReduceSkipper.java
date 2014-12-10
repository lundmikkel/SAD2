package engine;

public class ReduceSkipper implements Reducer {
    private final boolean unRoll;

    public ReduceSkipper(){
        unRoll = true;
    }

    public ReduceSkipper(boolean unRoll){
        this.unRoll = unRoll;
    }

    @Override
    public void reduce(Object k, Iterable iterable, Collector collector) {
        if (!unRoll)
            collector.collect(k,iterable);
        else
            for (Object v : iterable)
                collector.collect(k, v);
    }
}