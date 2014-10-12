package info.donsun.activator.util;

import info.donsun.activator.common.Constants;
import info.donsun.activator.common.PropertyName;
import info.donsun.activator.model.Application;
import info.donsun.activator.model.Entry;
import info.donsun.core.security.Digests;
import info.donsun.core.utils.Values;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public final class LoaderUtils {

    /**
     * 获取资源文件
     * 
     * @param entryDir
     * @param entry
     * @return
     */
    public static File getEntryFile(String rootDir, File entryDir, Entry entry) {
        String fileName = entry.getName();
        String version = entry.getVersion();
        if (!StringUtils.isEmpty(version)) {
            fileName += Constants.VERSION_SEPARATOR + version;
        }
        fileName += Constants.ENTRY_SUFFIEX;

        // 如果存在根目录，并且entryDir不是绝对路径
        if (!StringUtils.isEmpty(rootDir) && !entryDir.isAbsolute()) {
            return FileUtils.getFile(rootDir, entryDir.getPath(), fileName);
        }
        return FileUtils.getFile(entryDir, fileName);
    }

    /**
     * 加载资源
     * 
     * @throws IOException
     */
    public static File loadEntry(File entryDir, Entry entry, Application application) throws IOException {
        File entryFile = getEntryFile(application.getRootDir(), entryDir, entry);

        checkFile(entryDir, entryFile, entry.getChecksum(), application);

        if (!entryFile.exists() || !entryFile.isFile()) {

            throw new IOException(String.format("Load entry %s fail.", entry.getName()));
        }

        return entryFile;
    }

    public static void checkFile(File dir, File file, String checksum, Application application) throws IOException {
        if (!file.exists() || !file.isFile() || !checkMD5(file, Values.getString(checksum))) {
            if (isLocalPath(application.getBaseUrl())) {
                FileUtils.copyFile(FileUtils.getFile(application.getBaseUrl(), dir.getPath(), file.getName()), file);
            } else {
                URL fileUrl = createFileURL(dir, file.getName(), application.getBaseUrl());
                FileUtils.copyURLToFile(fileUrl, file, Values.getInt(application.getProperty(PropertyName.CONNECTION_TIMEOUT), Constants.DEFAULT_CONNECTION_TIMEOUT),
                        Values.getInt(application.getProperty(PropertyName.READ_TIMEOUT), Constants.DEFAULT_READ_TIMEOUT));
            }
        }
    }

    /**
     * 判断路径是否为一个已存在的本地路径
     * 
     * @param path
     * @return
     */
    public static boolean isLocalPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        return new File(path).exists();
    }
    
    /**
     * 是否是jar文件
     * 
     * @param url
     * @return
     */
    public static boolean isJarURL(URL url) {
        return url.toString().toLowerCase().endsWith(Constants.ENTRY_SUFFIEX);
    }

    /**
     * 获取真实地址，如果包含变量，则从系统属性中取出变量代表的地址<br>
     * 例如:%java.io.tmpdir%
     * 
     * @param path
     * @return
     */
    public static String getRealPath(String path) {
        if (!StringUtils.isEmpty(path)) {
            // 包含变量
            int end = 0;
            if (path.startsWith(Constants.VAR_REGEX) && (end = path.indexOf(Constants.VAR_REGEX, 1)) > 0) {

                String key = path.substring(1, end);
                Properties ps = System.getProperties();
                if (ps.containsKey(key)) {
                    return ps.getProperty(key) + path.substring(end + 2);
                }
            }

        }

        return path;
    }

    /**
     * 获取真实地址，如果包含%activator.root.dir%变量，则用rootDir替换之
     * 
     * @param path
     * @param rootDir
     * @return
     */
    public static String getRealPath(String path, String rootDir) {
        if (!StringUtils.isEmpty(path)) {
            if (StringUtils.contains(path, Constants.ROOT_DIR_VAR) && !StringUtils.isEmpty(rootDir)) {
                return StringUtils.replace(path, Constants.ROOT_DIR_VAR, rootDir);
            }

        }
        return path;
    }

    /**
     * 检查文件的MD5
     * 
     * @param file
     * @param checksum
     * @return
     * @throws IOException
     */
    private static boolean checkMD5(File file, String checksum) throws IOException {
        if (StringUtils.isEmpty(checksum)) {
            return true;
        }

        String fileMD5 = new String(Hex.encodeHex(Digests.md5(new FileInputStream(file))));
        return checksum.equalsIgnoreCase(fileMD5);
    }

    /**
     * 创建文件远程链接
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    private static URL createFileURL(File dir, String fileName, String baseUrl) throws IOException {
        String url = baseUrl;
        if (!url.endsWith(Constants.URL_SEPARATOR)) {
            url += Constants.URL_SEPARATOR;
        }
        url += StringUtils.replaceEach(dir.getPath(), new String[] { File.separator }, new String[] { Constants.URL_SEPARATOR });
        url += Constants.URL_SEPARATOR + fileName;

        return new URL(url);
    }

}
