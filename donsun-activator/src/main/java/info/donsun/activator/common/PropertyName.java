package info.donsun.activator.common;

/**
 * 属性名
 * 
 * @author Steven
 * 
 */
public interface PropertyName {

    /**
     * 扩展目录
     */
    String EXTENSION_DIR = "extension.dir";

    /**
     * 模块目录
     */
    String MODULES_DIR = "modules.dir";

    /**
     * 连接超时时长
     */
    String CONNECTION_TIMEOUT = "connection.timeout";

    /**
     * 读取超时时长
     */
    String READ_TIMEOUT = "read.timeout";

    /**
     * 是否清除未加载文件
     */
    String CLEAN_UNUSABLE = "clean.unusable";

}
