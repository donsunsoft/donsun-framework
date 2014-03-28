package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection;
import info.donsun.jwebsoup.executor.HttpClientExecutor;
import info.donsun.jwebsoup.util.Utils;
import info.donsun.jwebsoup.util.Validate;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 * Implementation of {@link Connection}.
 * 
 * @see info.donsun.jwebsoup.JWebsoup#connect(String)
 */
public final class ConnectionImpl implements Connection {

    private Request req;
    private Response res;
    private Executor exe;

    private ConnectionImpl() {
        req = RequestImpl.create();
        res = ResponseImpl.create(req);
        exe = HttpClientExecutor.create();
    }

    public static Connection create() {
        Connection con = new ConnectionImpl();
        return con;
    }

    public static Connection connect(String url) {
        return create().url(url);
    }

    public static Connection connect(URL url) {
        return create().url(url);
    }

    public Connection url(URL url) {
        req.url(url);
        return this;
    }

    public Connection url(String url) {
        Validate.notEmpty(url, "Must supply a valid URL");
        req.url(Utils.toURL(url));
        return this;
    }

    @Override
    public Connection executor(Executor executor) {
        Validate.notNull(executor, "Executor must not be null");
        this.exe = executor;
        return this;
    }

    public Connection userAgent(String userAgent) {
        Validate.notNull(userAgent, "User agent must not be null");
        req.header("User-Agent", userAgent);
        return this;
    }

    public Connection proxy(String host, int port) {
        return proxy(ProxyImpl.create(host, port));
    }

    public Connection proxy(Proxy proxy) {
        req.proxy(proxy);
        return this;
    }

    public Connection ssl(String path, String password) {
        return ssl(KeyStoreImpl.create(path, password));
    }

    public Connection ssl(KeyStore keyStore) {
        req.keyStore(keyStore);
        return this;
    }

    public Connection timeout(int millis) {
        req.timeout(millis);
        return this;
    }

    public Connection maxBodySize(int bytes) {
        req.maxBodySize(bytes);
        return this;
    }

    public Connection followRedirects(boolean followRedirects) {
        req.followRedirects(followRedirects);
        return this;
    }

    public Connection referrer(String referrer) {
        Validate.notNull(referrer, "Referrer must not be null");
        req.header("Referer", referrer);
        return this;
    }

    public Connection method(HttpMethod method) {
        req.method(method);
        return this;
    }

    public Connection data(String key, String value) {
        req.data(KeyValImpl.create(key, value));
        return this;
    }

    public Connection data(Map<String, String> data) {
        Validate.notNull(data, "Data map must not be null");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            req.data(KeyValImpl.create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public Connection data(String... keyvals) {
        Validate.notNull(keyvals, "Data key value pairs must not be null");
        Validate.isTrue(keyvals.length % 2 == 0, "Must supply an even number of key value pairs");
        for (int i = 0; i < keyvals.length; i += 2) {
            String key = keyvals[i];
            String value = keyvals[i + 1];
            Validate.notEmpty(key, "Data key must not be empty");
            Validate.notNull(value, "Data value must not be null");
            req.data(KeyValImpl.create(key, value));
        }
        return this;
    }

    public Connection data(Collection<KeyVal> data) {
        Validate.notNull(data, "Data collection must not be null");
        for (KeyVal entry : data) {
            req.data(entry);
        }
        return this;
    }

    public Connection header(String name, String value) {
        req.header(name, value);
        return this;
    }

    public Connection entity(Object entity) {
        req.entity(entity);
        return this;
    }

    public Connection headers(Map<String, String> headers) {
        Validate.notNull(headers, "Header map must not be null");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            req.header(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Connection cookie(Cookie cookie) {
        return cookies(cookie);
    }

    public Connection cookies(Collection<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            req.cookie(cookie);
        }
        return this;
    }

    @Override
    public Connection cookies(Map<String, String> cookies) {
        Validate.notNull(cookies, "Cooke map can't be null.");
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            req.cookie(CookieImpl.create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public Connection cookies(Cookie... cookies) {
        for (Cookie cookie : cookies) {
            req.cookie(cookie);
        }
        return this;
    }

    public Response get() throws IOException {
        req.method(HttpMethod.GET);
        return execute();
    }

    public Response post() throws IOException {
        req.method(HttpMethod.POST);
        return execute();
    }

    public Request request() {
        return req;
    }

    public Connection request(Request request) {
        req = request;
        return this;
    }

    public Response response() {
        return res;
    }

    public Connection response(Response response) {
        res = response;
        return this;
    }

    public Response execute() throws IOException {
        res = exe.execute(req);
        return res;
    }

}
