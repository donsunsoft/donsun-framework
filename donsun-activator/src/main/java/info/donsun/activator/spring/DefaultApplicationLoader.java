package info.donsun.activator.spring;

import info.donsun.activator.ApplicationLoader;
import info.donsun.activator.LoadEvent;
import info.donsun.activator.LoadListener;
import info.donsun.activator.common.Constants;
import info.donsun.activator.common.PropertyName;
import info.donsun.activator.model.Application;
import info.donsun.activator.model.Entry;
import info.donsun.activator.model.Extension;
import info.donsun.activator.model.Module;
import info.donsun.activator.model.Resource;
import info.donsun.activator.util.ClassLoaderUtils;
import info.donsun.activator.util.LoaderUtils;
import info.donsun.core.utils.Values;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DefaultApplicationLoader implements ApplicationLoader, ApplicationContextAware, InitializingBean {

    private String location;

    private Application application;

    private LoadListener loadListener;

    private ApplicationContext applicationContext;

    protected final Object lifecycleMonitor = new Object();

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    public void setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        synchronized (this.lifecycleMonitor) {
            loadApplication();

            if (Values.getBoolean(application.getProperty(PropertyName.CLEAN_UNUSABLE), true)) {

                try {

                    removeUnusableExt();
                    removeUnusableMod();
                } catch (IOException e) {
                    if (loadListener != null) {
                        loadListener.onLoad(new LoadEvent(application, LoadEvent.APPLICATION, "clean unusable fail."));
                    }
                }
            }
        }
    }

    protected void loadApplication() throws IOException {
        if (this.application == null) {
            Assert.notNull(location, "Location must be assigned.");

            if (location.startsWith(Constants.CLASSPATH_URL_PREFIX)) {
                this.application = parseApplicationScheme(this.getClass().getClassLoader().getResource(location.substring(Constants.CLASSPATH_URL_PREFIX.length())));
            } else if (location.startsWith(Constants.FILE_URL_PREFIX)) {
                File file = new File(location.substring(Constants.FILE_URL_PREFIX.length()));
                if (!file.exists()) {
                    throw new IllegalArgumentException("File not exists:" + file.getName());
                }
                this.application = parseApplicationScheme(file.toURI().toURL());

            } else if (location.startsWith(Constants.HTTP_URL_PREFIX) || location.startsWith(Constants.HTTPS_URL_PREFIX)) {
                this.application = parseApplicationScheme(new URL(location));
            } else {
                throw new IllegalArgumentException("Illegal location.");
            }

        }

        Assert.notNull(application.getBaseUrl(), "BaseUrl can't be null.");
        Assert.notNull(application.getRootDir(), "RootDir can't be null.");
        if (StringUtils.isEmpty(application.getProperty(PropertyName.EXTENSION_DIR))) {
            application.setProperty(PropertyName.EXTENSION_DIR, Constants.DEFAULT_EXT_DIR);
        }
        if (StringUtils.isEmpty(application.getProperty(PropertyName.MODULES_DIR))) {
            application.setProperty(PropertyName.MODULES_DIR, Constants.DEFAULT_MODULES_DIR);
        }

        String rootDir = LoaderUtils.getRealPath(application.getRootDir());
        application.setRootDir(rootDir);

        load(application);

    }

    private Application parseApplicationScheme(URL url) throws IOException {
        XmlMapper mapper = new XmlMapper();

        Application application = mapper.readValue(url, Application.class);
        if (StringUtils.isEmpty(application.getBaseUrl())) {
            application.setBaseUrl(getDefaultBaseUrl(url));
        }
        return application;
    }

    /**
     * 删除未加载的扩展
     * 
     * @throws IOException
     */
    private void removeUnusableExt() throws IOException {

        File extDir = FileUtils.getFile(application.getRootDir(), application.getProperty(PropertyName.EXTENSION_DIR));
        Extension ext = application.getExtension();
        if (ext == null) {
            FileUtils.forceDeleteOnExit(extDir);
        } else {
            if (extDir.exists() && extDir.isDirectory()) {
                // 加载成功的扩展
                Set<String> loadedExts = new HashSet<String>();

                List<Resource> resources = ext.getResources();
                if (resources != null && resources.size() > 0) {
                    for (Resource resource : resources) {
                        String resourceFileName = resource.getLocation();
                        File resourceFile = FileUtils.getFile(extDir, resourceFileName);
                        loadedExts.add(resourceFile.getAbsolutePath());
                    }
                }

                for (Entry entry : ext.getEntries()) {
                    loadedExts.add(LoaderUtils.getEntryFile(application.getRootDir(), extDir, entry).getAbsolutePath());
                }

                // for (File file : extDir.listFiles()) {
                // if (!loadedExts.contains(file.getAbsolutePath())) {
                // FileUtils.forceDelete(file);
                // }
                // }
                deleteFileExcept(extDir, loadedExts);

                deleteEmptyDirectory(extDir);

            }
        }

    }

    /**
     * 删除未加载的模块
     * 
     * @throws IOException
     */
    private void removeUnusableMod() throws IOException {

        File modsDir = FileUtils.getFile(application.getRootDir(), application.getProperty(PropertyName.MODULES_DIR));
        List<Module> mods = application.getModules();
        if (mods == null || mods.size() == 0) {
            FileUtils.forceDeleteOnExit(modsDir);
        } else {
            if (modsDir.exists() && modsDir.isDirectory()) {
                // 加载成功的模块
                Set<String> loadedMods = new HashSet<String>();
                for (Module mod : mods) {
                    File modDir = FileUtils.getFile(modsDir, mod.getName());

                    List<Resource> resources = mod.getResources();
                    if (resources != null && resources.size() > 0) {
                        for (Resource resource : resources) {
                            String resourceFileName = resource.getLocation();
                            File resourceFile = FileUtils.getFile(modDir, resourceFileName);
                            loadedMods.add(resourceFile.getAbsolutePath());
                        }
                    }

                    List<Entry> entries = mod.getEntries();
                    if (entries != null && entries.size() > 0) {
                        for (Entry entry : entries) {
                            if (!StringUtils.isEmpty(entry.getDir())) {
                                modDir = FileUtils.getFile(modDir, entry.getDir());
                            }

                            loadedMods.add(LoaderUtils.getEntryFile(application.getRootDir(), modDir, entry).getAbsolutePath());
                        }
                    }

                    String beanConfig = mod.getConfig();
                    if (!StringUtils.isEmpty(beanConfig)) {
                        for (String configFileName : beanConfig.trim().split(",")) {
                            File configFile = FileUtils.getFile(modDir, configFileName);
                            loadedMods.add(configFile.getAbsolutePath());
                        }
                    }

                }

                deleteFileExcept(modsDir, loadedMods);

                deleteEmptyDirectory(modsDir);

            }
        }

    }

    /**
     * 移除空目录
     * 
     * @param dir
     * @throws IOException
     */
    private void deleteEmptyDirectory(File dir) throws IOException {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                FileUtils.forceDelete(dir);
            } else {
                for (File file : files) {
                    deleteEmptyDirectory(file);
                }
            }
        }

    }

    /**
     * 删除目录下的文件，除了excludes
     * 
     * @param dir
     * @param excludes
     * @throws IOException
     */
    private void deleteFileExcept(File dir, Set<String> excludes) throws IOException {
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deleteFileExcept(file, excludes);
                } else {
                    if (!excludes.contains(file.getAbsolutePath())) {
                        FileUtils.forceDelete(file);
                    }

                }

            }
        }

    }

    /**
     * 获取默认根路径
     * 
     * @param url
     * @return
     */
    private String getDefaultBaseUrl(URL url) {
        String urlStr = url.toString();
        int pos = urlStr.lastIndexOf(Constants.URL_SEPARATOR);
        return urlStr.substring(0, pos);
    }

    private MessageSource getMessageSource(ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory.containsLocalBean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)) {
            return beanFactory.getBean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
        }
        return null;
    }

    public void load(Application application) throws IOException {

        // 加载ext
        Extension extension = application.getExtension();
        if (extension != null) {
            new ExtLoader().load(application, extension);
        }

        // 保存配置文件
        List<String> locationList = new ArrayList<String>();

        // 加载modules
        List<Module> modules = application.getModules();
        if (modules != null && modules.size() > 0) {
            for (Module module : modules) {
                new ModuleLoader().load(application, module, locationList);
            }
        }

        if (!locationList.isEmpty()) {
            // 加载配置文件
            new ConfigLoader().load(application, locationList.toArray(new String[0]));
        }
    }

    private class ConfigLoader {

        public void load(final Application application, String[] locations) {
            AbstractApplicationContext applicationContext = ((AbstractApplicationContext) getApplicationContext());
            ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

            DynamicLocationXmlApplicationContext reloadApplicationContext = new DynamicLocationXmlApplicationContext(locations, application.getRootDir());

            applicationContext.setParent(reloadApplicationContext);

            ConfigurableListableBeanFactory reloadBeanFactory = reloadApplicationContext.getBeanFactory();
            beanFactory.setParentBeanFactory(reloadBeanFactory);

            MessageSource reloadMessageSource = getMessageSource(reloadBeanFactory);
            if (reloadMessageSource != null) {
                MessageSource messageSource = getMessageSource(beanFactory);
                if (messageSource == null) {
                    DelegatingMessageSource dms = new DelegatingMessageSource();
                    dms.setParentMessageSource(reloadMessageSource);
                    beanFactory.registerSingleton(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME, dms);
                } else if (messageSource instanceof HierarchicalMessageSource) {
                    HierarchicalMessageSource hms = (HierarchicalMessageSource) messageSource;
                    if (hms.getParentMessageSource() == null) {
                        hms.setParentMessageSource(reloadMessageSource);
                    }

                }
            }

        }

    }

    private class EntryLoader {

        public void load(Application application, File entryDir, Entry entry) throws IOException {
            if (!StringUtils.isEmpty(entry.getDir())) {
                // local file system, eg: D:/temp
                if (entry.getDir().contains(":")) {
                    entryDir = new File(entry.getDir());
                } else {
                    entryDir = FileUtils.getFile(entryDir, entry.getDir());
                }
            }

            // if (!entryDir.exists() || !entryDir.isDirectory()) {
            // FileUtils.forceMkdir(entryDir);
            // }

            File entryFile = LoaderUtils.loadEntry(entryDir, entry, application);

            if (entryFile.exists() && entryFile.isFile()) {
                ClassLoaderUtils.loadJarFile(entryFile, applicationContext.getClassLoader());

            } else {
                throw new IOException(String.format("Load res %s fail.", entry.getName()));
            }

        }

    }

    private class ExtLoader {

        public void load(Application application, Extension extension) throws IOException {

            File extDir = new File(application.getProperty(PropertyName.EXTENSION_DIR));

            List<Resource> resources = extension.getResources();
            if (resources != null && resources.size() > 0) {
                for (Resource resource : resources) {
                    new ResourceLoader().load(application, extDir, resource);
                }

            }

            List<Entry> entries = extension.getEntries();
            if (entries != null && entries.size() > 0) {
                for (Entry entry : entries) {
                    if (loadListener != null) {
                        loadListener.onLoad(new LoadEvent(entry, LoadEvent.ENTRY, String.format("Loading extension %s...", entry.getName())));
                    }
                    new EntryLoader().load(application, extDir, entry);
                }
            }

        }

    }

    private class ModuleLoader {

        public void load(final Application application, Module module, List<String> locationList) throws IOException {

            File modulesDir = new File(application.getProperty(PropertyName.MODULES_DIR));

            File moduleDir = FileUtils.getFile(modulesDir, module.getName());

            // 加载资源
            List<Resource> resources = module.getResources();
            if (resources != null && resources.size() > 0) {
                for (Resource resource : resources) {
                    new ResourceLoader().load(application, moduleDir, resource);
                }

            }

            // 加载模块
            List<Entry> entries = module.getEntries();
            if (entries != null && entries.size() > 0) {
                for (Entry entry : entries) {
                    if (loadListener != null) {
                        loadListener.onLoad(new LoadEvent(entry, LoadEvent.ENTRY, String.format("Loading module %s...", entry.getName())));
                    }
                    new EntryLoader().load(application, moduleDir, entry);
                }
            }

            // 加载配置文件
            String beanConfig = module.getConfig();
            if (!StringUtils.isEmpty(beanConfig)) {

                for (String configFileName : StringUtils.deleteWhitespace(beanConfig).split(Constants.CONFIG_SPLIT)) {
                    if (ResourceUtils.isUrl(configFileName)) {
                        locationList.add(configFileName);
                    } else {
                        File configFile = FileUtils.getFile(application.getRootDir(), moduleDir.getPath(), configFileName);
                        String dir = StringUtils.remove(configFileName, configFile.getName());
                        if (loadListener != null) {
                            loadListener.onLoad(new LoadEvent(configFileName, LoadEvent.CONFIG, String.format("Loading spring config %s...", configFileName)));
                        }

                        LoaderUtils.checkFile(!StringUtils.isEmpty(dir) ? FileUtils.getFile(moduleDir, dir) : moduleDir, configFile, Constants.VAR_REGEX, application);

                        if (configFile.exists() && configFile.isFile()) {
                            locationList.add(configFile.getPath());
                        } else {
                            throw new IOException(String.format("Load config file %s fail.", configFile.getName()));
                        }
                    }
                }

            }

        }

    }

    private class ResourceLoader {

        public void load(Application application, File resourceDir, Resource resource) throws IOException {

            String resourceFileName = LoaderUtils.getRealPath(resource.getLocation(), application.getRootDir());

            if (!ResourceUtils.isUrl(resourceFileName)) {

                File resourceFile = FileUtils.getFile(application.getRootDir(), resourceDir.getPath(), resourceFileName);

                String dir = StringUtils.remove(resourceFileName, resourceFile.getName());
                if (loadListener != null) {
                    loadListener.onLoad(new LoadEvent(resourceFileName, LoadEvent.RESOURCE, String.format("Loading resource %s...", resourceFileName)));
                }

                LoaderUtils.checkFile(!StringUtils.isEmpty(dir) ? FileUtils.getFile(resourceDir, dir) : resourceDir, resourceFile, resource.getChecksum(), application);
            }
        }

    }

}
