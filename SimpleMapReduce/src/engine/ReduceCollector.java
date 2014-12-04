package engine;

import java.util.List;
import java.util.Vector;

public class ReduceCollector<K, V> implements Collector<K, V> {
    public final List<Tuple<K, V>> list = new Vector<>();

    @Override
    public void collect(K k, V v) {
        list.add(new Tuple<>(k,v));
    }
}
