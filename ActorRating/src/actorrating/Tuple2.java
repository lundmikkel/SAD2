package actorrating;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Rasmus on 03-12-2014.
 */
public class Tuple2<T1,T2> implements Serializable {
    T1 t1;
    T2 t2;
    public Tuple2(T1 t1, T2 t2){
        this.t1 = t1;
        this.t2 = t2;
    }
    public T1 first(){
        return t1;
    }
    public T2 second(){
        return t2;
    }

}
