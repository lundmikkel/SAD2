package engine;

public interface Reducer<InKey, InValue, OutKey, OutValue> {
    public void reduce(InKey key, Iterable<InValue> values, Collector<OutKey, OutValue> collector);
}