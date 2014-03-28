package info.donsun.jwebsoup.executor;

import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.Connection.Executor;
import info.donsun.jwebsoup.Connection.KeyVal;
import info.donsun.jwebsoup.Connection.HttpMethod;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.impl.CookieImpl;
import info.donsun.jwebsoup.impl.ResponseImpl;
import info.donsun.jwebsoup.util.Utils;
import info.donsun.jwebsoup.util.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Implementation of {@link Executor} by Apache HttpClient.
 * 
 * @author Steven
 * 
 */
public class HttpClientExecutor implements Executor {

    public static HttpClientExecutor create() {
        return new HttpClientExecutor();
    }

    private HttpClientExecutor() {
    }

    @Override
    public Response execute(Request req) throws IOException {
        Validate.notNull(req, "Request must not be null");
        String protocol = req.url().getProtocol();
        if (!protocol.equals("http") && !protocol.equals("https")) {
            throw new MalformedURLException("Only http & https protocols supported");
        }

        HttpClient client = createHttpClient(req);
        HttpUriRequest httpRequest = null;
        try {
            if (req.method() == HttpMethod.POST) {
                httpRequest = new HttpPost(req.url().toString());
                ((HttpPost) httpRequest).setEntity(generateHttpEntity(req));

            } else if (req.method() == HttpMethod.DELETE) {
                Utils.serialiseRequestUrl(req); // appends query string
                httpRequest = new HttpDelete(req.url().toString());
            } else if (req.method() == HttpMethod.PUT) {
                httpRequest = new HttpPut(req.url().toString());
                ((HttpPut) httpRequest).setEntity(generateHttpEntity(req));
            } else {
                Utils.serialiseRequestUrl(req); // appends query string
                httpRequest = new HttpGet(req.url().toString());
            }

            httpRequest.setHeaders(createHeaders(req));

            HttpContext httpContext = new BasicHttpContext();

            HttpResponse httpResponse = client.execute(httpRequest, httpContext);

            return generateResponse(req, httpResponse, httpContext);
        } finally {
            if (httpRequest != null && httpRequest instanceof HttpEntityEnclosingRequestBase) {
                ((HttpEntityEnclosingRequestBase) httpRequest).releaseConnection();
            }
            client.getConnectionManager().shutdown();
        }

    }

    /**
     * Create httpClient
     * 
     * @param request the request object
     * @return HttpClient
     * @throws ClientProtocolException
     */
    private HttpClient createHttpClient(Request request) throws ClientProtocolException {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, request.charset() != null ? request.charset() : Utils.defaultCharset);
        // set a default userAgent, it will be overwrite by header's value
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, Utils.defaultUserAgent);
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, request.timeout());
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, request.timeout());
        client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, request.followRedirects());
        client.getParams().setParameter(ClientPNames.MAX_REDIRECTS, request.maxRedirects());
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, request.allowCircularRedirects());

        // intercept request to set gzip
        client.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader(HttpHeaders.ACCEPT_ENCODING)) {
                    request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");
                }
            }

        });

        // intercept response to set gzip
        client.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Header ceheader = entity.getContentEncoding();
                    if (ceheader != null) {
                        HeaderElement[] codecs = ceheader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }
            }

        });

        // set proxy
        if (request.proxy() != null) {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(request.proxy().host(), request.proxy().port()));
            if (request.proxy().username() != null) {
                AuthScope authscope = new AuthScope(request.proxy().host(), request.proxy().port());
                Credentials credentials = new UsernamePasswordCredentials(request.proxy().username(), request.proxy().password());
                client.getCredentialsProvider().setCredentials(authscope, credentials);
            }
        }

        // if protocol is https, try to set keyStore.
        String protocol = request.url().getProtocol();
        if (protocol.equals("https")) {
            try {
                SSLSocketFactory socketFactory = null;
                // has keyStore file
                if (request.keyStore() != null && request.keyStore().password() != null) {
                    java.security.KeyStore trustStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
                    FileInputStream instream = new FileInputStream(new File(request.keyStore().path()));
                    try {
                        trustStore.load(instream, request.keyStore().password().toCharArray());
                    } finally {
                        try {
                            if (instream != null) {
                                instream.close();
                            }
                        } catch (Exception ignore) {
                            // do nothing
                        }
                    }
                    socketFactory = new SSLSocketFactory(trustStore);
                }
                // has no keyStore file
                else {
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    X509TrustManager tm = new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                        }
                    };
                    ctx.init(null, new TrustManager[] { tm }, null);
                    socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                }

                Scheme sch = new Scheme("https", 443, socketFactory);
                client.getConnectionManager().getSchemeRegistry().register(sch);
            } catch (Exception e) {
                throw new ClientProtocolException("Load keyStore fail.", e);
            }

        }
        return client;
    }

    private HttpEntity generateHttpEntity(Request req) throws IOException {
        if (req.entity() != null) {
            if (req.entity() instanceof HttpEntity) {
                return (HttpEntity) req.entity();
            } else {
                String jsonStr = Utils.getJsonMapper().writeValueAsString(req.entity());
                req.header("Content-Type", "application/json; charset=" + req.charset());
                return new StringEntity(jsonStr, req.charset());
            }
        } else {
            List<NameValuePair> nvpsa = new ArrayList<NameValuePair>();
            if (req.data().size() > 0) {
                for (KeyVal data : req.data()) {
                    nvpsa.add(new BasicNameValuePair(data.key(), data.value()));
                }
            }
            return new UrlEncodedFormEntity(nvpsa, req.charset());
        }

    }

    private Header[] createHeaders(Request req) {
        List<Header> headerList = new ArrayList<Header>();

        Collection<Cookie> cookies = req.cookies();
        if (!cookies.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (Cookie cookie : cookies) {
                if (sb.length() > 0) {
                    sb.append("; ");
                }
                sb.append(cookie.name() + "=" + cookie.value());
            }
            req.header("Cookie", sb.toString());
        }

        Map<String, String> headers = req.headers();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerList.add(new BasicHeader(header.getKey(), header.getValue()));
        }

        return headerList.toArray(new Header[0]);
    }

    // set up header, cookies
    private Response generateResponse(Request req, HttpResponse httpResponse, HttpContext httpContext) throws IOException {

        Response res = ResponseImpl.create(req);
        // statusCode and statusMessage
        StatusLine statusLine = httpResponse.getStatusLine();
        res.statusCode(statusLine.getStatusCode());
        res.statusMessage(statusLine.getReasonPhrase());

        CookieSpec spec = (CookieSpec) httpContext.getAttribute(ClientContext.COOKIE_SPEC);
        CookieOrigin origin = (CookieOrigin) httpContext.getAttribute(ClientContext.COOKIE_ORIGIN);

        // response headers
        processResponseHeaders(res, httpResponse.getAllHeaders(), spec, origin);

        HttpEntity entity = httpResponse.getEntity();
        try {
            // contentType from header
            Header hct = entity.getContentType();
            if (hct != null) {
                res.contentType(hct.getValue());
                // may be null, readInputStream deals with it
                String charset = Utils.getCharsetFromContentType(res.contentType());
                if (charset != null) {
                    res.charset(charset);
                }
            }

            res.bodyAsBytes(EntityUtils.toByteArray(entity));
            res.contentLength(res.bodyAsBytes().length);
            return res;
        } finally {
            EntityUtils.consume(entity);
        }

    }

    private void processResponseHeaders(Response res, Header[] resHeaders, CookieSpec spec, CookieOrigin origin) {
        if (resHeaders == null) {
            return;
        }
        for (Header header : resHeaders) {

            String name = header.getName();
            if (name == null) {
                continue; // http/1.1 line
            }

            // set response header
            res.header(name, header.getValue());

            // set response cookie
            if (name.equalsIgnoreCase("Set-Cookie")) {
                try {
                    List<org.apache.http.cookie.Cookie> cookies = spec.parse(header, origin);
                    if (cookies != null && cookies.size() > 0) {
                        for (org.apache.http.cookie.Cookie c : cookies) {
                            Cookie cookie = CookieImpl.create(c.getName(), c.getValue(), c.getDomain(), c.getPath(), c.getExpiryDate());
                            cookie.secure(c.isSecure());
                            res.cookie(cookie);
                        }
                    }
                } catch (MalformedCookieException e) {
                    // do nothing
                }
            }

        }
    }

}
