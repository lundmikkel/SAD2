package engine;

public interface Mapper<InKey, InValue, OutKey, OutValue> {
    public void map(InKey key, InValue value, Collector<OutKey, OutValue> collector);
}