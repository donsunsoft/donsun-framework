package info.donsun.activator.spring;

import info.donsun.activator.util.LoaderUtils;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

/**
 * 动态路径XmlApplicationContext<br>
 * 
 * 从文件系统、classpath、URL中加载spring的xml配置文件
 * 
 * @author Steven
 * 
 */
public class DynamicLocationXmlApplicationContext extends AbstractXmlApplicationContext {

    private String rootDir;

    private String[] locations;

    public DynamicLocationXmlApplicationContext(String[] locations, String rootDir) {
        this.locations = locations;
        this.rootDir = rootDir;

        refresh();
    }

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        location = LoaderUtils.getRealPath(location, rootDir);
        if (ResourceUtils.isUrl(location)) {
            return super.getResource(location);
        }
        return new FileSystemResource(location);
    }

    @Override
    protected String[] getConfigLocations() {
        return locations;
    }

}
