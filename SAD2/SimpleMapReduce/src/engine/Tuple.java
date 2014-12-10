package engine;

public class Tuple<K,V> {
    public final K key;
    public final V value;

    public Tuple(K k, V v){
        this.key = k;
        this.value = v;
    }

    // region Overrides

    @Override
    public String toString() {
        return "(" + key.toString() + " : " + value.toString() + ")";
    }

    @Override
    public int hashCode() {
        return (17 + key.hashCode()) * 31 + value.hashCode();
    }

    @Override
     public boolean equals(Object obj) {
        // Not strictly necessary, but often a good optimization
        if (this == obj)
        return true;
        if (!(obj instanceof Tuple))
            return false;
        Tuple that = (Tuple) obj;
        return this.key.equals(that.key) && this.value.equals(that.value);
    }

    // endregion
}