package info.donsun.app.activator;

import info.donsun.activator.common.Constants;
import info.donsun.core.security.Digests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class ApplicationXmlGenerator {

    private static Logger logger = Logger.getLogger(ApplicationXmlGenerator.class);

    private File sourceDir = new File("D:\\Temp\\fromurl");

    private boolean isChecksum = true;

    // application
    private String appId = "test";
    private String appName = "测试工程";
    private String appVersion = "2.4.0";
    private String baseUrl = "D:\\Temp\\fromurl";
    private String rootDir = "%java.io.tmpdir%/donsunsoft";

    // property
    private String extensionDir = "";
    private String modulesDir = "";
    private Integer connectionTimeout;
    private Integer readTimeout;

    @Before
    public void parepare() {
        if (StringUtils.isEmpty(extensionDir)) {
            extensionDir = "extension";
        }
        if (StringUtils.isEmpty(modulesDir)) {
            modulesDir = "modules";
        }

    }

    @Test
    public void generate() throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(generateApplicationHeader());
        sb.append(generateProperty());

        // ext

        sb.append(generateExt());

        // modules

        sb.append(generateMod());
        sb.append(generateApplicationFooter());

        System.out.println(sb.toString());

    }

    private Object generateMod() throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();

        File modRootDir = FileUtils.getFile(sourceDir, modulesDir);
        if (modRootDir.exists() && modRootDir.isDirectory()) {

            File[] modsDir = modRootDir.listFiles();
            if (modsDir != null && modsDir.length > 0) {
                sb.append("<modules>");
                for (File modDir : modsDir) {

                    if (modDir.exists() && modDir.isDirectory()) {
                        sb.append(String.format("<module name=\"%s\">", modDir.getName()));
                        sb.append(generateResources(modDir));
                        sb.append(generateConfig(modDir));

                        File[] entriesFile = modDir.listFiles();
                        if (entriesFile != null && entriesFile.length > 0) {
                            sb.append("<entries>");
                            for (File entryFile : entriesFile) {
                                sb.append(generateEntry(modDir, entryFile));
                            }
                            sb.append("</entries>");
                        }

                        sb.append("</module>");

                    }
                }
                sb.append("</modules>");
            }
        }

        return sb.toString();
    }

    private String generateExt() throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();

        File extDir = FileUtils.getFile(sourceDir, extensionDir);


        if (extDir.exists() && extDir.isDirectory()) {
            File[] files = extDir.listFiles();
            if (files != null && files.length > 0) {
                sb.append("<extension>");
                sb.append(generateResources(extDir));
                sb.append("<entries>");
                for (File entryFile : files) {
                    sb.append(generateEntry(extDir, entryFile));
                }
                sb.append("</entries>");
                sb.append("</extension>");
            }
        }

        return sb.toString();
    }

    public void testEntry() throws FileNotFoundException, IOException {
        File entryDir = new File("D:\\testdoc\\ext");
        File entry = new File("D:\\testdoc\\ext\\apache\\castor-1.0.1.jar");

        String s = generateEntry(entryDir, entry);
        System.out.println(s);

        File configFileDir = new File("D:\\testdoc\\modules\\mgj");
        String s1 = generateConfig(configFileDir);
        System.out.println(s1);

        String s2 = generateProperty();
        System.out.println(s2);

    }

    // <?xml version="1.0" encoding="UTF-8"?>
    // <application id="xx" name="nonono" version="1.1.1">
    private String generateApplicationHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append(String.format("<application id=\"%s\" name=\"%s\" version=\"%s\" baseUrl=\"%s\" rootDir=\"%s\" >", appId, appName, appVersion, baseUrl, rootDir));

        return sb.toString();
    }

    private String generateApplicationFooter() {
        return "</application>";
    }

    private boolean isNeedProperty() {
        return !StringUtils.isEmpty(extensionDir) || !StringUtils.isEmpty(modulesDir) || connectionTimeout != null || readTimeout != null;
    }

    // <property>
    // <extensionDir>ext</extensionDir>
    // </property>
    private String generateProperty() {
        StringBuffer sb = new StringBuffer();
        if (isNeedProperty()) {
            sb.append("<properties>");
            if (!StringUtils.isEmpty(extensionDir)) {
                sb.append(String.format("<property name=\"extension.dir\" value=\"%s\" />", extensionDir));
            }
            if (!StringUtils.isEmpty(modulesDir)) {
                sb.append(String.format("<property name=\"modules.dir\" value=\"%s\" />", modulesDir));
            }
            if (connectionTimeout != null) {
                sb.append(String.format("<property name=\"connection.timeout\" value=\"%s\" />", connectionTimeout));
            }
            if (readTimeout != null) {
                sb.append(String.format("<property name=\"read.timeout\" value=\"%s\" />", readTimeout));
            }
            sb.append("</properties>");

        }
        return sb.toString();

    }

    // <resources>
    // <resource location="message1.properties" />
    // <resource location="lang/messages2_en.properties" />
    // <resource location="lang/messages2_zh_CN.properties" />
    // </resources>
    private String generateResources(File resourceDir) throws FileNotFoundException, IOException {

        File[] configFiles = resourceDir.listFiles();

        Map<String, String> list = new HashMap<String, String>();
        if (configFiles != null) {
            for (File file : configFiles) {
                Map<String, String> tmp = parseResourceFile(resourceDir, file);
                list.putAll(tmp);

            }
        }

        StringBuffer sb = new StringBuffer();
        if (!list.isEmpty()) {
            sb.append("<resources>");
            for (Entry<String, String> entry : list.entrySet()) {
                String resource = entry.getKey();
                String checksum = "";
                if (isChecksum) {

                    checksum = "checksum=\"" + entry.getValue() + "\"";
                }

                sb.append(String.format("<resource location=\"%s\" %s />", resource, checksum));
            }
            // sb.append(StringUtils.join(list.toArray(new String[0]), ","));
            sb.append("</resources>");
        }
        return sb.toString();

    }

    private Map<String, String> parseResourceFile(File resourceDir, File resourceFile) throws FileNotFoundException, IOException {
        Map<String, String> map = new HashMap<String, String>();
        String fileName = resourceFile.getName();
        if (resourceFile.isFile() && !fileName.toLowerCase().endsWith(".xml") && !fileName.toLowerCase().endsWith(Constants.ENTRY_SUFFIEX)) {
            String key = StringUtils.remove(resourceFile.getPath(), resourceDir.getPath());
            String value = generateMD5(resourceFile);
            map.put(key, value);
        } else if (resourceFile.isDirectory()) {
            File[] files = resourceFile.listFiles();
            for (File file : files) {
                map.putAll(parseResourceFile(resourceDir, file));
            }
        }
        return map;
    }

    // <beanConfig>messages-config.xml</beanConfig>
    private String generateConfig(File modDir) {

        File[] configFiles = modDir.listFiles();
        StringBuffer sb = new StringBuffer();

        List<String> list = new ArrayList<String>();
        for (File file : configFiles) {
            List<String> tmp = parseConfigFile(modDir, file);
            list.addAll(tmp);

        }
        if (!list.isEmpty()) {
            sb.append("<config>");
            sb.append(StringUtils.join(list.toArray(new String[0]), ","));
            sb.append("</config>");
        }
        return sb.toString();

    }

    private List<String> parseConfigFile(File modDir, File configFile) {
        List<String> list = new ArrayList<String>();
        String fileName = configFile.getName();
        if (fileName.toLowerCase().endsWith(".xml")) {
            list.add(StringUtils.remove(configFile.getPath(), modDir.getPath()));
        } else if (configFile.isDirectory()) {
            File[] files = configFile.listFiles();
            for (File file : files) {
                list.addAll(parseConfigFile(modDir, file));
            }
        }
        return list;
    }

    // <entry>
    // <name>messages_en</name>
    // <suffix>.properties</suffix>
    // <checksum>sa</checksum>
    // </entry>
    private String generateEntry(File entryDir, File entry) throws FileNotFoundException, IOException {
        if (entry.isDirectory()) {
            File[] entries = entry.listFiles();
            StringBuffer ssb = new StringBuffer();
            for (File sentry : entries) {
                ssb.append(generateEntry(entryDir, sentry));
            }
            return ssb.toString();
        }
        logger.debug("generating entry " + entry.getName());
        StringBuffer sb = new StringBuffer();

        String suffix = getEntrySuffix(entry.getName());
        if (Constants.ENTRY_SUFFIEX.equals(suffix)) {

            sb.append("<entry>");
            sb.append(String.format("<name>%s</name>", getEntryName(entry.getName())));
            sb.append(String.format("<version>%s</version>", getEntryVersion(entry.getName())));

            String dir = getSubDir(entryDir, entry);
            if (!StringUtils.isEmpty(dir)) {
                sb.append(String.format("<dir>%s</dir>", dir));
            }

            if (isChecksum) {
                sb.append(String.format("<checksum>%s</checksum>", generateMD5(entry)));
            }
            sb.append("</entry>");
        }

        return sb.toString();

    }

    /**
     * 计算文件md5
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String generateMD5(File file) throws FileNotFoundException, IOException {
        return new String(Hex.encodeHex(Digests.md5(new FileInputStream(file))));
    }

    /**
     * 获取文件名
     * 
     * @param fileName
     * @return
     */
    private String getEntryName(String fileName) {
        int pos = fileName.lastIndexOf("-");
        return fileName.substring(0, pos);
    }

    /**
     * 获取文件版本
     * 
     * @param fileName
     * @return
     */
    private String getEntryVersion(String fileName) {
        int vpos = fileName.lastIndexOf("-");
        int spos = fileName.lastIndexOf(".");
        return fileName.substring(vpos + 1, spos);
    }

    /**
     * 获取文件后缀
     * 
     * @param fileName
     * @return
     */
    private String getEntrySuffix(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos).toLowerCase();
    }

    /**
     * 获取文件子目录
     * 
     * @param parentDir
     * @param file
     * @return
     */
    private String getSubDir(File parentDir, File file) {
        String parentDirPath = parentDir.getPath();
        String filePath = file.getPath();
        String fileName = file.getName();

        String dir = filePath.replace(parentDirPath, "").replace(fileName, "");
        if (dir.length() > 1) {
            return dir.substring(1, dir.length() - 1);
        } else {
            return "";
        }

    }

}
