package engine;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MapCollector<Key, Value> implements Collector<Key, Value>{
    final ConcurrentHashMap<Key, Vector<Value>> map = new ConcurrentHashMap<>();

    public void collect(Key k, Value v){
        map.putIfAbsent(k, new Vector<>());
        map.get(k).add(v);
    }
}
