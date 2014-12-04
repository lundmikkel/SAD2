package engine;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Rasmus on 04-12-2014.
 */
public class MapCollector<Key, Value> implements Collector<Key, Value>{

    public final ConcurrentHashMap<Key, Vector<Value>> map = new ConcurrentHashMap<Key, Vector<Value>>();

    public void collect(Key k, Value v){
        map.putIfAbsent(k, new Vector<Value>());
        map.get(k).add(v);
    }
}
