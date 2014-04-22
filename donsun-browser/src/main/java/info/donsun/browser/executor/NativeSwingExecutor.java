package info.donsun.browser.executor;

import info.donsun.browser.JBrowser;
import info.donsun.browser.JBrowser.CookieOption;
import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.Connection.Executor;
import info.donsun.jwebsoup.Connection.KeyVal;
import info.donsun.jwebsoup.Connection.HttpMethod;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.impl.ResponseImpl;
import info.donsun.jwebsoup.util.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import chrriis.dj.nativeswing.NativeSwing;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationParameters;

/**
 * Implementation of {@link Executor} by NativeSwing. This implement only works
 * in awt projects.
 * 
 * @author Steven
 * 
 */
public class NativeSwingExecutor implements Executor {

    private static final String SUCCESS_MESSAGE_LOCALE = "\u5b8c\u6210";

    private static final String SUCCESS_MESSAGE = "OK";

    private static final String BLANK_LOCATION = "about:blank";

    static {
        if (!NativeInterface.isInitialized()) {
            NativeSwing.initialize();
            NativeInterface.open();
            if (!NativeInterface.isEventPumpRunning()) {
                Thread eventThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NativeInterface.runEventPump();
                    }
                });
                eventThread.setDaemon(true);
                eventThread.start();
            }
        }
    }

    public static NativeSwingExecutor create() {
        return new NativeSwingExecutor();
    }

    @Override
    public Response execute(final Request request) throws IOException {

        final Response response = ResponseImpl.create(request);

        // JBrowser must running in AWT thread.
        if (!JBrowser.isRunningInAwtThread()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        doExecute(request, response);
                    }
                });
            } catch (InterruptedException e) {
                // do nothing
            } catch (InvocationTargetException e) {
                throw new IOException(e.getTargetException());
            }
        } else {
            doExecute(request, response);
        }

        return response;
    }

    private void doExecute(final Request request, final Response response) {

        final JBrowser browser = JBrowser.createSimpleBrowser();

        // browser must be add to JFrame
        new JFrame().add(browser);
        browser.initializeNativePeer();

        // read start time
        final long start = System.currentTimeMillis();

        browser.runInSequence(new Runnable() {

            @Override
            public void run() {
                try {

                    initCookies(browser, request);

                    try {
                        WebBrowserNavigationParameters parameters = prepareParameters(request);
                        browser.navigate(request.url().toString(), parameters);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String content = null;
                    String location = null;
                    String statusText = null;
                    int progress;

                    boolean loading = true;
                    do {
                        // if timeout, directly return
                        if (System.currentTimeMillis() - start > request.timeout()) {
                            response.statusMessage("timeout");
                            response.statusCode(HttpStatus.SC_REQUEST_TIMEOUT);
                            return;
                        }

                        // loading location
                        location = browser.getResourceLocation();
                        // loading status
                        statusText = browser.getStatusText();
                        // loading progress
                        progress = browser.getLoadingProgress();

                        System.out.println("================>location:" + location + " status:" + statusText + " progress:" + progress);

                        // get web content
                        content = browser.getHTMLContent();

                        if (progress == 100 && !BLANK_LOCATION.equals(location) && (SUCCESS_MESSAGE_LOCALE.equals(statusText) || SUCCESS_MESSAGE.equalsIgnoreCase(statusText))) {
                            loading = false;
                        }
                        if (loading) {
                            // wait a moment.
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // do nothing
                            }
                            continue;
                        }
                    } while (loading);

                    if (content != null) {
                        String charset = browser.getPageCharset();
                        if (!StringUtils.isEmpty(charset)) {
                            response.charset(charset);
                        }
                        byte[] byteContent = !StringUtils.isEmpty(charset) ? content.getBytes(Charset.forName(charset)) : content.getBytes();
                        response.bodyAsBytes(byteContent);
                        response.contentLength(byteContent.length);
                        // this implementation can't get contentType

                        response.statusMessage(statusText);
                        response.statusCode(HttpStatus.SC_OK);

                        // this implementation can't get response headers, but
                        // it can get cookies.
                        response.cookies(browser.getCookies());

                    }
                } finally {
                    browser.disposeNativePeer();
                }
            }
        });

    }

    private void initCookies(JBrowser browser, Request request) {
        Collection<Cookie> cookies = request.cookies();
        if (cookies != null && cookies.size() > 0) {
            MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);
            for (Cookie cookie : cookies) {
                Map<CookieOption, Object> options = new HashMap<CookieOption, Object>();
                if (cookie.path() != null) {
                    options.put(CookieOption.path, cookie.path());
                }
                if (cookie.domain() != null) {
                    options.put(CookieOption.domain, cookie.domain());
                }
                if (cookie.expires() != null) {
                    long millis = cookie.expires().getTime() - System.currentTimeMillis();
                    double days = new BigDecimal(millis).divide(new BigDecimal("8.64e7"), mc).doubleValue();
                    options.put(CookieOption.days, days);
                }
                browser.addCookie(cookie.name(), cookie.value(), options);
            }
        }
    }

    private WebBrowserNavigationParameters prepareParameters(Request request) throws IOException {
        WebBrowserNavigationParameters parameters = new WebBrowserNavigationParameters();
        Collection<KeyVal> datas = request.data();
        if (datas != null && datas.size() > 0) {
            if (request.method() == HttpMethod.GET || request.method() == HttpMethod.DELETE) {
                Utils.serialiseRequestUrl(request);
            } else if (request.method() == HttpMethod.POST || request.method() == HttpMethod.PUT) {
                Map<String, String> keyValueMap = new HashMap<String, String>();
                for (KeyVal data : datas) {
                    keyValueMap.put(data.key(), data.value());
                }
                parameters.setPostData(keyValueMap);
            }
        }
        parameters.setHeaders(request.headers());

        return parameters;
    }
}
