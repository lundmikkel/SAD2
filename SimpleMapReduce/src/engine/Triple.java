package engine;

public class Triple<T1, T2, T3> {
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;
    public Triple(T1 a, T2 b, T3 c){
        item1 = a;
        item2 = b;
        item3 = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triple triple = (Triple) o;

        if (!item1.equals(triple.item1)) return false;
        if (!item2.equals(triple.item2)) return false;
        if (!item3.equals(triple.item3)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = item1.hashCode();
        result = 31 * result + item2.hashCode();
        result = 31 * result + item3.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + item1 + " : " + item2 + " : " + item3 + ")";
    }
}
