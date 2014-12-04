package engine;

import java.util.List;
import java.util.Vector;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class ReduceCollector<K, V> implements Collector<K, V> {

    public final List<Tuple<K, V>> list = new Vector<>();

    @Override
    public void collect(K k, V v) {
        list.add(new Tuple<K, V>(k,v));
    }
}
