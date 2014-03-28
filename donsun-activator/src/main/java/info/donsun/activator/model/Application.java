package info.donsun.activator.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "application")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    /**
     * 应用程序ID
     */
    @JacksonXmlProperty(isAttribute = true)
    private String id;

    /**
     * 应用程序名称
     */
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    /**
     * 应用程序版本
     */
    @JacksonXmlProperty(isAttribute = true)
    private String version;

    /**
     * 文件下载基础路径，为空则代表和配置文件相同路径
     */
    @JacksonXmlProperty(isAttribute = true)
    private String baseUrl;

    /**
     * 根目录
     */
    @JacksonXmlProperty(isAttribute = true)
    private String rootDir;

    // private String mainClass;
    //
    // private String executeMethod;
    //
    // private Property property;

    private Set<Property> properties;

    private Extension extension;

    private List<Module> modules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // public String getMainClass() {
    // return mainClass;
    // }
    //
    // public void setMainClass(String mainClass) {
    // this.mainClass = mainClass;
    // }
    //
    // public String getExecuteMethod() {
    // return executeMethod;
    // }
    //
    // public void setExecuteMethod(String executeMethod) {
    // this.executeMethod = executeMethod;
    // }
    //
    // public Property getProperty() {
    // return property;
    // }
    //
    // public void setProperty(Property property) {
    // this.property = property;
    // }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @JsonIgnore
    public void setProperty(String name, String value) {
        if (properties == null) {
            properties = new HashSet<Property>();
        }
        Property property = new Property();
        property.setName(name);
        property.setValue(value);
        properties.add(property);
    }

    @JsonIgnore
    public String getProperty(String name, String defaultValue) {
        if (properties != null && properties.size() > 0) {
            for (Property property : properties) {
                if (property != null && name.equals(property.getName())) {
                    return property.getValue() != null ? property.getValue() : defaultValue;
                }
            }
        }

        return defaultValue;
    }

    @JsonIgnore
    public String getProperty(String name) {
        return getProperty(name, null);
    }

}
