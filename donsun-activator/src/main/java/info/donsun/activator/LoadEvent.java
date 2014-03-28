package info.donsun.activator;

import java.util.EventObject;

/**
 * 加载事件
 * 
 * @author Steven
 * 
 */
public class LoadEvent extends EventObject {

    private static final long serialVersionUID = 4780931004316994217L;

    public static final int APPLICATION = 1;

    public static final int EXTENSION = 2;

    public static final int MODULE = 3;

    public static final int ENTRY = 4;

    public static final int CONFIG = 5;

    public static final int RESOURCE = 6;

    private int type;

    private String message;

    public LoadEvent(Object source, int type, String message) {
        super(source);
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
