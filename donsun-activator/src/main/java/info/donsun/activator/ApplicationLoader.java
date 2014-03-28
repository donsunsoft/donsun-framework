package info.donsun.activator;

import info.donsun.activator.model.Application;

import java.io.IOException;

/**
 * 应用程序加载器接口
 * 
 * @author Steven
 * 
 */
public interface ApplicationLoader {
    
    /**
     * 获取应用程序信息
     * 
     * @return 应用程序
     */
    Application getApplication();

    /**
     * 加载
     * 
     * @param application 应用程序
     */
    void load(Application application) throws IOException;
}
