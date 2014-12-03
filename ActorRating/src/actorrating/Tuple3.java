package actorrating;

import java.io.Serializable;

/**
 * Created by Rasmus on 03-12-2014.
 */
public class Tuple3<T1,T2,T3> implements Serializable {
    T1 t1;
    T2 t2;
    T3 t3;
    public Tuple3(T1 t1, T2 t2, T3 t3){
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }
    public T1 first(){
        return t1;
    }
    public void setFirst(T1 t1){this.t1 = t1;}
    public T2 second(){
        return t2;
    }
    public void setSecond(T2 t2){this.t2 = t2;}
    public T3 third(){
        return t3;
    }
    public void setThird(T3 t3){this.t3 = t3;}
}
