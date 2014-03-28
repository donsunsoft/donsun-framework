package info.donsun.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private String path;

    private Properties config;

    public Config() {
    }

    public Config(String path) {
        this.config = new Properties();
        setPath(path);
    }

    private void loadConfig() {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(path);
            config.clear();
            config.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveConfig(String comments) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path);
            config.store(fos, comments);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveConfig() {
        saveConfig(null);
    }

    private void checkExist() {
        File file = new File(path);
        File parent = file.getParentFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void savePropertie(String key, String value, String comments) {
        setPropertie(key, value);
        saveConfig(comments);
    }

    public void savePropertie(String key, String value) {
        savePropertie(key, value, null);
    }

    public void setPropertie(String key, String value) {
        config.setProperty(key, value);
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        value = value == null ? defaultValue : value;
        return value;
    }

    public void remove(Object key) {
        config.remove(key);
    }

    public void removeAndSave(Object key) {
        remove(key);
        saveConfig();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        checkExist();
        loadConfig();
    }
}