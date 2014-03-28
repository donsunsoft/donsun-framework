package info.donsun.jwebsoup.util;

import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.Connection.KeyVal;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.impl.CookieImpl;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public final class Utils {

    public static final String defaultCharset = "UTF-8";

    public static final String defaultUserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36";

    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");

    public static String encodeUrl(String url) {
        if (url == null) {
            return null;
        }
        return url.replaceAll(" ", "%20");
    }

    public static URL toURL(String url) {
        try {
            return new URL(encodeUrl(url));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + url, e);
        }
    }

    /**
     * for get url reqs, serialise the data map into the url
     * 
     * @param req
     * @throws IOException
     */
    public static void serialiseRequestUrl(Request req) throws IOException {
        if (req.data().size() > 0) {
            URL in = req.url();
            StringBuilder url = new StringBuilder();
            boolean first = true;
            // reconstitute the query, ready for appends includes host, port
            url.append(in.getProtocol()).append("://").append(in.getAuthority()).append(in.getPath()).append("?");
            if (in.getQuery() != null) {
                url.append(in.getQuery());
                first = false;
            }
            for (KeyVal keyVal : req.data()) {
                if (!first) {
                    url.append('&');
                } else {
                    first = false;
                }
                url.append(URLEncoder.encode(keyVal.key(), defaultCharset)).append('=').append(URLEncoder.encode(keyVal.value(), defaultCharset));
            }
            req.url(new URL(url.toString()));
        }
    }

    public static String getCharsetFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
        Matcher m = charsetPattern.matcher(contentType);
        if (m.find()) {
            String charset = m.group(1).trim();
            charset = charset.replace("charset=", "");
            if (charset.isEmpty())
                return null;
            try {
                if (Charset.isSupported(charset))
                    return charset;
                charset = charset.toUpperCase(Locale.ENGLISH);
                if (Charset.isSupported(charset))
                    return charset;
            } catch (IllegalCharsetNameException e) {
                // if our advanced charset matching fails.... we just take the
                // default
                return null;
            }
        }
        return null;
    }

    public static ObjectMapper getJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }

    public static XmlMapper getXmlMapper() {
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + "'s superclass not ParameterizedType");
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length <= 0) {
            throw new IllegalArgumentException("Index: " + 0 + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
        }

        if (!(params[0] instanceof Class)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
        }
        return (Class) params[0];
    }

    public static Map<String, String> parseCookies(String value) {
        return parseCookies(value, ",");
    }

    public static Map<String, String> parseCookies(String value, String separatorChars) {
        Map<String, String> cookies = new HashMap<String, String>();
        String[] tokens = StringUtils.split(value, separatorChars);
        if (tokens != null && tokens.length > 0) {
            for (String token : tokens) {
                String[] keyval = StringUtils.split(token, "=");
                if (keyval != null && keyval.length == 2) {
                    cookies.put(StringUtils.trimToEmpty(keyval[0]), StringUtils.trimToEmpty(keyval[1]));
                }
            }
        }
        return cookies;
    }

    public static Cookie parseCookie(String value) {
        return parseCookie(value, ";");
    }

    public static Cookie parseCookie(String value, String separatorChars) {
        Cookie cookie = null;
        String[] tokens = StringUtils.split(value, separatorChars);
        if (tokens != null && tokens.length > 0) {
            for (String token : tokens) {
                String[] keyval = StringUtils.split(token, "=");
                if (keyval != null && keyval.length == 2) {
                    String key = StringUtils.trimToEmpty(keyval[0]);
                    String val = StringUtils.trimToEmpty(keyval[1]);
                    if (cookie == null) {
                        cookie = CookieImpl.create(key, val);
                    } else {
                        if ("expires".equals(key)) {
                            GregorianCalendar calendar = new GregorianCalendar();
                            DateFormat formatter = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
                            try {
                                calendar.setTime(formatter.parse(val));
                                cookie.expires(calendar.getTime());
                            } catch (ParseException e) {
                                // time parse error.
                            }
                        } else if ("path".equals(key)) {
                            cookie.path(val);
                        } else if ("domain".equals(key)) {
                            cookie.domain(val);
                        } else if ("secure".equals(key)) {
                            cookie.secure(BooleanUtils.toBoolean(val));
                        }
                    }
                }
            }
        }
        return cookie;
    }

}
