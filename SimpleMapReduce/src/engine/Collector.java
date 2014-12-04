package engine;

public interface Collector<Key, Value> {
    public void collect(Key k, Value v);
}