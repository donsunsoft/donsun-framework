package info.donsun.jwebsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * A Connection provides a convenient interface to fetch content from the web,
 * and parse them into Documents.
 * <p>
 * To get a new Connection, use
 * {@link info.donsun.jwebsoup.JWebsoup#connect(String)}. Connections contain
 * {@link Connection.Request} and {@link Connection.Response} objects. The
 * request objects are reusable as prototype requests.
 * <p>
 * Request configuration can be made using either the shortcut methods in
 * Connection (e.g. {@link #userAgent(String)}), or by methods in the
 * Connection.Request object directly. All request configuration must be made
 * before the request is executed.
 */
public interface Connection {

    /**
     * http methods.
     */
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE
    }

    /**
     * Set the request URL to fetch. The protocol must be HTTP or HTTPS.
     * 
     * @param url URL to connect to
     * @return this Connection, for chaining
     */
    public Connection url(URL url);

    /**
     * Set the request URL to fetch. The protocol must be HTTP or HTTPS.
     * 
     * @param url URL to connect to
     * @return this Connection, for chaining
     */
    public Connection url(String url);

    /**
     * Set the executor to handle http request
     * 
     * @param executor new executor
     * @return this Connection, for chaining
     */
    public Connection executor(Executor executor);

    /**
     * Set the request user-agent header.
     * 
     * @param userAgent user-agent to use
     * @return this Connection, for chaining
     */
    public Connection userAgent(String userAgent);

    /**
     * Set keyStorePath and keyStorePassword for ssl
     * 
     * @param path keyStore path for ssl
     * @param password keyStorePassword
     * @return this Connection, for chaining
     */
    public Connection ssl(String path, String password);

    /**
     * Set keyStore for ssl
     * 
     * @param keyStore keyStore object
     * @return this Connection, for chaining
     */
    public Connection ssl(KeyStore keyStore);

    /**
     * Set proxy host and port
     * 
     * @param host proxy ip or domain
     * @param port proxy port number
     * @return this Connection, for chaining
     */
    public Connection proxy(String host, int port);

    /**
     * Set proxy
     * 
     * @param proxy proxy object
     * @return this Connection, for chaining
     */
    public Connection proxy(Proxy proxy);

    /**
     * Set the request timeouts (connect and read). If a timeout occurs, an
     * IOException will be thrown. The default timeout is 3 seconds (3000
     * millis). A timeout of zero is treated as an infinite timeout.
     * 
     * @param millis number of milliseconds (thousandths of a second) before
     *            timing out connects or reads.
     * @return this Connection, for chaining
     */
    public Connection timeout(int millis);

    /**
     * Set the maximum bytes to read from the (uncompressed) connection into the
     * body, before the connection is closed, and the input truncated. The
     * default maximum is 1MB. A max size of zero is treated as an infinite
     * amount (bounded only by your patience and the memory available on your
     * machine).
     * 
     * @param bytes number of bytes to read from the input before truncating
     * @return this Connection, for chaining
     */
    public Connection maxBodySize(int bytes);

    /**
     * Set the request referrer (aka "referer") header.
     * 
     * @param referrer referrer to use
     * @return this Connection, for chaining
     */
    public Connection referrer(String referrer);

    /**
     * Configures the connection to (not) follow server redirects. By default
     * this is <b>true</b>.
     * 
     * @param followRedirects true if server redirects should be followed.
     * @return this Connection, for chaining
     */
    public Connection followRedirects(boolean followRedirects);

    /**
     * Set the request method to use, GET or POST. Default is GET.
     * 
     * @param method HTTP request method
     * @return this Connection, for chaining
     */
    public Connection method(HttpMethod method);

    /**
     * Add a request data parameter. Request parameters are sent in the request
     * query string for GETs, and in the request body for POSTs. A request may
     * have multiple values of the same name, and they are sequential.
     * 
     * @param key data key
     * @param value data value
     * @return this Connection, for chaining
     */
    public Connection data(String key, String value);

    /**
     * Adds all of the supplied data to the request data parameters
     * 
     * @param data collection of data parameters
     * @return this Connection, for chaining
     */
    public Connection data(Collection<KeyVal> data);

    /**
     * Adds all of the supplied data to the request data parameters
     * 
     * @deprecated map can not ensure the ordering.
     * @param data map of data parameters
     * @return this Connection, for chaining
     */
    @Deprecated
    public Connection data(Map<String, String> data);

    /**
     * Add a number of request data parameters. Multiple parameters may be set
     * at once, e.g.:
     * <code>.data("name", "jsoup", "language", "Java", "language", "English");</code>
     * creates a query string like:
     * <code>?name=jsoup&language=Java&language=English</code>
     * 
     * @param keyvals a set of key value pairs.
     * @return this Connection, for chaining
     */
    public Connection data(String... keyvals);

    /**
     * Add entity to request, it can be any implements of
     * org.apache.http.HttpEntity.<br>
     * If other object is assigned, it will be consider as json object, and
     * convert by under method:<br>
     * 
     * <code> String jsonStr = DataUtil.getJsonMapper().writeValueAsString(req.entity());</code>
     * <code>((HttpPost) httpRequest).setEntity(new StringEntity(jsonStr, req.charset()));</code>
     * 
     * <ul>
     * <li>
     * If entity assigned, all data are ignore.</li>
     * <li>
     * The entity is work only post request executed.</li>
     * </ul>
     * 
     * @param entity
     * @return
     */
    public Connection entity(Object entity);

    /**
     * Set a request header.
     * 
     * @param name header name
     * @param value header value
     * @return this Connection, for chaining
     * @see info.donsun.jwebsoup.Connection.Request#headers()
     */
    public Connection header(String name, String value);

    /**
     * Adds all of the supplied headers
     * 
     * @param headers map
     * @return this Connection, for chaining
     */
    public Connection headers(Map<String, String> headers);

    /**
     * Set a cookie to be sent in the request.
     * 
     * @param cookie cookie object
     * @return this Connection, for chaining
     */
    public Connection cookie(Cookie cookie);

    /**
     * Adds cookies to the request.
     * 
     * @param cookies cookie collection
     * @return this Connection, for chaining
     */
    public Connection cookies(Collection<Cookie> cookies);

    /**
     * Adds cookies to the request.
     * 
     * @param cookies cookie map
     * @return this Connection, for chaining
     */
    public Connection cookies(Map<String, String> cookies);

    /**
     * Adds cookies to the request.
     * 
     * @param cookies cookie array
     * @return this Connection, for chaining
     */
    public Connection cookies(Cookie... cookies);

    /**
     * Execute the request as a GET, and parse the result.
     * 
     * @return a response object
     * @throws java.net.MalformedURLException if the request URL is not a HTTP
     *             or HTTPS URL, or is otherwise malformed
     * @throws java.net.SocketTimeoutException if the connection times out
     * @throws IOException on error
     */
    public Response get() throws IOException;

    /**
     * Execute the request as a POST, and parse the result.
     * 
     * @return a response object
     * @throws java.net.MalformedURLException if the request URL is not a HTTP
     *             or HTTPS URL, or is otherwise malformed
     * @throws java.net.SocketTimeoutException if the connection times out
     * @throws IOException on error
     */
    public Response post() throws IOException;

    /**
     * Execute the request.
     * 
     * @return a response object
     * @throws java.net.MalformedURLException if the request URL is not a HTTP
     *             or HTTPS URL, or is otherwise malformed
     * @throws java.net.SocketTimeoutException if the connection times out
     * @throws IOException on error
     */
    public Response execute() throws IOException;

    /**
     * Get the request object associated with this connection
     * 
     * @return request a request object
     */
    public Request request();

    /**
     * Set the connection's request
     * 
     * @param request new request object
     * @return this Connection, for chaining
     */
    public Connection request(Request request);

    /**
     * Get the response, once the request has been executed
     * 
     * @return response a response object
     */
    public Response response();

    /**
     * Set the connection's response
     * 
     * @param response new response
     * @return this Connection, for chaining
     */
    public Connection response(Response response);

    /**
     * Common methods for Requests and Responses
     * 
     * @param <T> Type of Base, either Request or Response
     */
    @SuppressWarnings("rawtypes")
    public interface Base<T extends Base> {

        /**
         * Get the value of a header. This is a simplified header model, where a
         * header may only have one value.
         * <p>
         * Header names are case insensitive.
         * 
         * @param name name of header (case insensitive)
         * @return value of header, or null if not set.
         * @see #hasHeader(String)
         * @see #cookie(String)
         */
        public String header(String name);

        /**
         * Set a header. This method will overwrite any existing header with the
         * same case insensitive name.
         * 
         * @param name Name of header
         * @param value Value of header
         * @return this, for chaining
         */
        public T header(String name, String value);

        /**
         * Set headers. This method will overwrite any existing header with the
         * same case insensitive name.
         * 
         * @param headers headers map
         * @return this, for chaining
         */
        public T headers(Map<String, String> headers);

        /**
         * Check if a header is present
         * 
         * @param name name of header (case insensitive)
         * @return if the header is present in this request/response
         */
        public boolean hasHeader(String name);

        /**
         * Remove a header by name
         * 
         * @param name name of header to remove (case insensitive)
         * @return this, for chaining
         */
        public T removeHeader(String name);

        /**
         * Retrieve all of the request/response headers as a map
         * 
         * @return headers
         */
        public Map<String, String> headers();

        /**
         * Get a cookie by name from this request/response.
         * <p>
         * Response objects have a simplified cookie model. Each cookie set in
         * the response is added to the response object's cookie key=value map.
         * The cookie's path, domain, and expiry date are ignored.
         * 
         * @param name name of cookie to retrieve.
         * @return value of cookie, or null if not set
         */
        public Cookie cookie(String name);

        /**
         * Set a cookie in this request/response.
         * 
         * @param name name of cookie
         * @param value value of cookie
         * @return this, for chaining
         */
        public T cookie(Cookie cookie);

        /**
         * Set cookies in this request/response.
         * 
         * @param cookies cookies collection
         * @return this, for chaining
         */
        public T cookies(Collection<Cookie> cookies);

        /**
         * Set cookies in this request/response.
         * 
         * @param cookies cookies map
         * @return this, for chaining
         */
        public T cookies(Map<String, String> cookies);

        /**
         * Check if a cookie is present
         * 
         * @param name name of cookie
         * @return if the cookie is present in this request/response
         */
        public boolean hasCookie(String name);

        /**
         * Remove a cookie by name
         * 
         * @param name name of cookie to remove
         * @return this, for chaining
         */
        public T removeCookie(String name);

        /**
         * Retrieve all of the request/response cookies as a collection
         * 
         * @return cookies
         */
        public Collection<Cookie> cookies();

        /**
         * Get the character set name of the request/response.
         * 
         * @return character set name
         */
        public String charset();

        /**
         * Set character set name to request/response.
         * 
         * @param charset character set name
         * @return this, for chaining
         */
        public T charset(String charset);

    }

    /**
     * Represents a HTTP request.
     */
    public interface Request extends Base<Request> {

        /**
         * Get the URL
         * 
         * @return URL
         */
        public URL url();

        /**
         * Set the URL
         * 
         * @param url new URL
         * @return this, for chaining
         */
        public Request url(URL url);

        /**
         * Get the request method
         * 
         * @return method
         */
        public HttpMethod method();

        /**
         * Set the request method
         * 
         * @param method new method
         * @return this, for chaining
         */
        public Request method(HttpMethod method);

        /**
         * Get the request proxy
         * 
         * @return the proxy
         */
        public Proxy proxy();

        /**
         * Update the request proxy
         * 
         * @param proxy new proxy
         * @return this Request, for chaining
         */
        public Request proxy(Proxy proxy);

        /**
         * Get the request keyStore
         * 
         * @return the keyStore
         */
        public KeyStore keyStore();

        /**
         * Update the request keyStore
         * 
         * @param keyStore new keyStore
         * @return this Request, for chaining
         */
        public Request keyStore(KeyStore keyStore);

        /**
         * Get the request timeout, in milliseconds.
         * 
         * @return the timeout in milliseconds.
         */
        public int timeout();

        /**
         * Update the request timeout.
         * 
         * @param millis timeout, in milliseconds
         * @return this Request, for chaining
         */
        public Request timeout(int millis);

        /**
         * Get the maximum body size, in milliseconds.
         * 
         * @return the maximum body size, in milliseconds.
         */
        public int maxBodySize();

        /**
         * Update the maximum body size, in milliseconds.
         * 
         * @param bytes maximum body size, in milliseconds.
         * @return this Request, for chaining
         */
        public Request maxBodySize(int bytes);

        /**
         * Get the current followRedirects configuration.
         * 
         * @return true if followRedirects is enabled.
         */
        public boolean followRedirects();

        /**
         * Configures the request to (not) follow server redirects. By default
         * this is <b>true</b>.
         * 
         * @param followRedirects true if server redirects should be followed.
         * @return this Request, for chaining
         */
        public Request followRedirects(boolean followRedirects);

        /**
         * Get the current maxRedirects configuration.
         * 
         * @return maxRedirects
         */
        public int maxRedirects();

        /**
         * Update maxRedirects
         * 
         * @param maxRedirects new maxRedirects
         * @return this Request, for chaining
         */
        public Request maxRedirects(int maxRedirects);

        /**
         * Get the current allowCircularRedirects configuration.
         * 
         * @return allowCircularRedirects
         */
        public boolean allowCircularRedirects();

        /**
         * Update allowCircularRedirects
         * 
         * @param allowCircularRedirects new allowCircularRedirects
         * @return this Request, for chaining
         */
        public Request allowCircularRedirects(boolean allowCircularRedirects);

        /**
         * Add a data parameter to the request
         * 
         * @param keyval data to add.
         * @return this Request, for chaining
         */
        public Request data(KeyVal keyval);

        /**
         * Get all of the request's data parameters
         * 
         * @return collection of keyvals
         */
        public Collection<KeyVal> data();

        /**
         * Get entity if post request.
         * 
         * @return the entity object
         */
        public Object entity();

        /**
         * Set entity to request
         * 
         * @param entity new entity
         * @return this Request, for chaining
         */
        public Request entity(Object entity);

    }

    /**
     * Represents a HTTP response.
     */
    public interface Response extends Base<Response> {

        /**
         * Get the status code of the response.
         * 
         * @return status code
         */
        public int statusCode();

        /**
         * Set the statusCode
         * 
         * @param statusCode new statusCode
         * @return this Response, for chaining
         */
        public Response statusCode(int statusCode);

        /**
         * Get the status message of the response.
         * 
         * @return status message
         */
        public String statusMessage();

        /**
         * Set the statusMessage
         * 
         * @param statusMessage new statusMessage
         * @return this Response, for chaining
         */
        public Response statusMessage(String statusMessage);

        /**
         * Get the response content type (e.g. "text/html");
         * 
         * @return the response content type
         */
        public String contentType();

        /**
         * Set the contentType
         * 
         * @param contentType new contentType
         * @return this Response, for chaining
         */
        public Response contentType(String contentType);

        /**
         * Get the response content length.
         * 
         * @return the response content length
         */
        public long contentLength();

        /**
         * Set the contentLength
         * 
         * @param contentLength new contentLength
         * @return this Response, for chaining
         */
        public Response contentLength(long contentLength);

        /**
         * Parse the body of the response
         * 
         * @param <T>
         * 
         * @return a parsed Object
         * @throws IOException on error
         */
        public <T> T parse(Parser<T> parser) throws IOException;

        /**
         * Get the body of the response as a plain string.
         * 
         * @return body
         */
        public String body();

        /**
         * Get the body of the response as an array of bytes.
         * 
         * @return body bytes
         */
        public byte[] bodyAsBytes();

        /**
         * Set the bodyAsBytes
         * 
         * @param bodyAsBytes new bodyAsBytes
         * @return this Response, for chaining
         */
        public Response bodyAsBytes(byte[] bodyAsBytes);
    }

    /**
     * A Key Value tuple.
     */
    public interface KeyVal {

        /**
         * Update the key of a keyval
         * 
         * @param key new key
         * @return this KeyVal, for chaining
         */
        public KeyVal key(String key);

        /**
         * Get the key of a keyval
         * 
         * @return the key
         */
        public String key();

        /**
         * Update the value of a keyval
         * 
         * @param value the new value
         * @return this KeyVal, for chaining
         */
        public KeyVal value(String value);

        /**
         * Get the value of a keyval
         * 
         * @return the value
         */
        public String value();
    }

    /**
     * The Cookie
     */
    public interface Cookie {

        /**
         * Returns the name.
         * 
         * @return String name The name
         */
        public String name();

        /**
         * Update the name of a cookie
         * 
         * @param name new name
         * @return this Cookie, for chaining
         */
        public Cookie name(String name);

        /**
         * Returns the value.
         * 
         * @return String value The current value.
         */
        public String value();

        /**
         * Update the value of a cookie
         * 
         * @param value new value
         * @return this Cookie, for chaining
         */
        public Cookie value(String value);

        /**
         * Returns the expiration {@link Date} of the cookie, or <tt>null</tt>
         * if none exists.
         * <p>
         * <strong>Note:</strong> the object returned by this method is
         * considered immutable. Changing it (e.g. using setTime()) could result
         * in undefined behaviour. Do so at your peril.
         * </p>
         * 
         * @return Expiration {@link Date}, or <tt>null</tt>.
         */
        public Date expires();

        /**
         * Update the expires of a cookie
         * 
         * @param expires new expires
         * @return this Cookie, for chaining
         */
        public Cookie expires(Date expires);

        /**
         * Returns domain attribute of the cookie. The value of the Domain
         * attribute specifies the domain for which the cookie is valid.
         * 
         * @return the value of the domain attribute.
         */
        public String domain();

        /**
         * Update the domain of a cookie
         * 
         * @param domain new domain
         * @return this Cookie, for chaining
         */
        public Cookie domain(String domain);

        /**
         * Returns the path attribute of the cookie. The value of the Path
         * attribute specifies the subset of URLs on the origin server to which
         * this cookie applies.
         * 
         * @return The value of the path attribute.
         */
        public String path();

        /**
         * Update the path of a cookie
         * 
         * @param path new path
         * @return this Cookie, for chaining
         */
        public Cookie path(String path);

        /**
         * Indicates whether this cookie requires a secure connection.
         * 
         * @return <code>true</code> if this cookie should only be sent over
         *         secure connections, <code>false</code> otherwise.
         */
        public boolean secure();

        /**
         * Update the secure of a cookie
         * 
         * @param secure new secure
         * @return this Cookie, for chaining
         */
        public Cookie secure(boolean secure);

        /**
         * Returns <tt>false</tt> if the cookie should be discarded at the end
         * of the "session"; <tt>true</tt> otherwise.
         * 
         * @return <tt>false</tt> if the cookie should be discarded at the end
         *         of the "session"; <tt>true</tt> otherwise
         */
        public boolean persistent();

    }

    /**
     * Proxy
     */
    public interface Proxy {
        /**
         * Get proxy host, ip or domain.
         * 
         * @return the host
         */
        public String host();

        /**
         * Update the host of a proxy
         * 
         * @param host new host
         * @return this Proxy, for chaining
         */
        public Proxy host(String host);

        /**
         * Get proxy port.
         * 
         * @return the port
         */
        public int port();

        /**
         * Update the port of a proxy
         * 
         * @param port new port
         * @return this Proxy, for chaining
         */
        public Proxy port(int port);

        /**
         * Get proxy username if need.
         * 
         * @return the username
         */
        public String username();

        /**
         * Update the username of a proxy
         * 
         * @param username new username
         * @return this Proxy, for chaining
         */
        public Proxy username(String username);

        /**
         * Get prxoy password if need.
         * 
         * @return the password
         */
        public String password();

        /**
         * Update the password of a proxy
         * 
         * @param password new password
         * @return this Proxy, for chaining
         */
        public Proxy password(String password);

    }

    /**
     * KeyStore
     */
    public interface KeyStore {

        /**
         * Get keyStore path
         * 
         * @return the keyStore path
         */
        public String path();

        /**
         * Set keyStore path
         * 
         * @param path new keyStore path
         * @return this KeyStore, for chaining
         */
        public KeyStore path(String path);

        /**
         * Get keyStore password
         * 
         * @return the keyStore password
         */
        public String password();

        /**
         * Set keyStore password
         * 
         * @param password new keyStore password
         * @return this KeyStore, for chaining
         */
        public KeyStore password(String password);
    }

    /**
     * Executor interface
     */
    public interface Executor {

        /**
         * execute request, return the response
         * 
         * @param request request object
         * @return response object
         * 
         * @throws IOException
         */
        Response execute(Request request) throws IOException;

    }

    /**
     * Web parser interface
     * 
     * @param <T>
     */
    public interface Parser<T> {

        /**
         * Parse web by request and response
         * 
         * @param request a request object
         * @param response a response object
         * @return the result
         * @throws IOException
         */
        T parse(Request request, Response response) throws IOException;
    }

}
