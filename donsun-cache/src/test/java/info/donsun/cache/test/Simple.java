package info.donsun.cache.test;

import java.io.Serializable;

public class Simple implements Serializable {

    private static final long serialVersionUID = -5819717830913266718L;

    private int id;
    private String name;

    public Simple() {
    }

    public Simple(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sample [id=" + id + ", name=" + name + "]";
    }

}
