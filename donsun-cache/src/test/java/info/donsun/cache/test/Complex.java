package info.donsun.cache.test;

import java.io.Serializable;

public class Complex implements Serializable {

    private static final long serialVersionUID = -4771795149448493231L;

    private int id;
    private Simple simple;

    public Complex() {
    }

    public Complex(int id, Simple simple) {
        this.id = id;
        this.simple = simple;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Simple getSimple() {
        return simple;
    }

    public void setSimple(Simple simple) {
        this.simple = simple;
    }

    @Override
    public String toString() {
        return "Complex [id=" + id + ", simple=" + simple + "]";
    }

}
