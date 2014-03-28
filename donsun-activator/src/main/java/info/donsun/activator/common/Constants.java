package info.donsun.activator.common;

public interface Constants {

    // 默认连接超时时间
    int DEFAULT_CONNECTION_TIMEOUT = 30000;

    // 默认读取超时时间
    int DEFAULT_READ_TIMEOUT = 30000;

    // 默认扩展包目录
    String DEFAULT_EXT_DIR = "extension";

    // 默认模块目录
    String DEFAULT_MODULES_DIR = "modules";

    // url分隔符
    String URL_SEPARATOR = "/";

    // 版本分隔符
    String VERSION_SEPARATOR = "-";

    // 条目后缀
    String ENTRY_SUFFIEX = ".jar";

    // classpath URL前缀
    String CLASSPATH_URL_PREFIX = "classpath:";

    // 文件URL前缀
    String FILE_URL_PREFIX = "file:";

    // http URL前缀
    String HTTP_URL_PREFIX = "http:";

    // https URL前缀
    String HTTPS_URL_PREFIX = "https";

    // 配置文件分隔符
    String CONFIG_SPLIT = ",";

    // 变量表达式
    String VAR_REGEX = "%";

    /**
     * Activator的rootDir路径变量
     */
    String ROOT_DIR_VAR = Constants.VAR_REGEX + "activator.root.dir" + Constants.VAR_REGEX;
}
