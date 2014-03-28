package info.donsun.browser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class JBrowser extends JWebBrowser {
    private static final long serialVersionUID = -4559898003383734575L;

    /**
     * cookie管理js
     */
    private static String COOKIE_MANAGE_JS = "";

    /**
     * 输出所有cookie的js
     */
    private static final String PRINT_COOKIE_JS = "return document.cookie;";

    /**
     * cookie选项
     */
    public enum CookieOption {
        /**
         * 有效天数
         */
        days,
        /**
         * 路径
         */
        path,
        /**
         * 域
         */
        domain,
        /**
         * 是否受保护
         */
        secure
    }

    private JBrowser() {
        try {
            COOKIE_MANAGE_JS = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("js/cookie.js").getPath()));
        } catch (IOException e) {
            throw new IllegalStateException("Load js/cookie.js fail.", e);
        }
    }

    public static JBrowser createBrowser() {
        return new JBrowser();
    }

    public static JBrowser createSimpleBrowser() {
        JBrowser browser = createBrowser();
        browser.setStatusBarVisible(false);
        browser.setButtonBarVisible(false);
        browser.setBarsVisible(false);
        browser.setLocationBarVisible(false);
        browser.setDefaultPopupMenuRegistered(false);
        return browser;
    }

    public Map<String, String> getCookies() {
        Map<String, String> cookies = new HashMap<String, String>();
        Object obj = executeJavascriptWithResult(PRINT_COOKIE_JS);
        if (obj != null) {
            String[] tokens = StringUtils.split(obj.toString(), ";");
            if (tokens != null && tokens.length > 0) {
                for (String token : tokens) {
                    String[] keyVal = StringUtils.split(token, "=");
                    if (keyVal != null && keyVal.length == 2) {
                        cookies.put(StringUtils.trimToEmpty(keyVal[0]), keyVal[1]);
                    }
                }
            }

        }
        return cookies;
    }

    public String getCookie(String name) {
        Object obj = executeJavascriptWithResult(COOKIE_MANAGE_JS + String.format("return cookie.get('%s');", name));
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public void addCookie(String name, String value) {
        addCookie(name, value, null);
    }

    public void addCookie(String name, String value, Map<CookieOption, Object> options) {
        String executeMethod = null;
        if (options != null) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<CookieOption, Object> entry : options.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }

                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(entry.getKey().name());
                sb.append(":");
                sb.append("'");
                sb.append(entry.getValue());
                sb.append("'");
            }
            String opt = "{" + sb.toString() + "}";
            executeMethod = COOKIE_MANAGE_JS + String.format("cookie.set('%s','%s',%s);", name, value, opt);
        } else {
            executeMethod = COOKIE_MANAGE_JS + String.format("cookie.set('%s','%s');", name, value);
        }
        executeJavascript(executeMethod);
    }

    public void removeCookie(String name, String path, String domain) {
        executeJavascript(COOKIE_MANAGE_JS + String.format("cookie.remove('%s', '%s', '%s')", name, path, domain));
    }

    public void cleanCookies(String path, String domain) {
        for (Map.Entry<String, String> entry : getCookies().entrySet()) {
            removeCookie(entry.getKey(), path, domain);
        }
    }

    public String getPageCharset() {
        String charset = null;
        String charsetJs = "return document.characterSet;";
        if ("ie".equalsIgnoreCase(getBrowserType())) {
            charsetJs = "return document.charset;";
        }
        Object obj = executeJavascriptWithResult(charsetJs);
        if (obj != null) {
            charset = StringUtils.trim(obj.toString());
        }
        return charset;

    }

    public String getPageLanguage() {
        String language = null;
        String languageJs = "return window.navigator.language;";
        if ("ie".equalsIgnoreCase(getBrowserType())) {
            languageJs = "return window.navigator.systemLanguage;";
        }
        Object obj = executeJavascriptWithResult(languageJs);
        if (obj != null) {
            language = StringUtils.trim(obj.toString());
        }
        return language;

    }
}
