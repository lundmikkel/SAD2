package engine;

public class Tuple<K,V> {
    public final K key;
    public final V value;

    public Tuple(K k, V v){
        this.key = k;
        this.value = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        if (!key.equals(tuple.key)) return false;
        if (!value.equals(tuple.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + key.toString() + " : " + value.toString() + ")";
    }
}