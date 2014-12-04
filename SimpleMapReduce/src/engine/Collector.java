package engine;

/**
 * Created by Rasmus on 04-12-2014.
 */
public interface Collector<Key, Value> {
    public void collect(Key k, Value v);
}
