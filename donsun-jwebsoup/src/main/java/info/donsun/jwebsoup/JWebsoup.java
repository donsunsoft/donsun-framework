package info.donsun.jwebsoup;

import info.donsun.jwebsoup.Connection.Executor;
import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.impl.ConnectionImpl;

import java.io.IOException;

/**
 * JWebsoup entrance
 * 
 * @author Steven
 * 
 */
public class JWebsoup {
    private JWebsoup() {
    }

    /**
     * Create a connection, then do something by call chaining methods.
     * <p>
     * examples:
     * <ul>
     * <li>
     * <code>Response response = JWebsoup.connect("http://example.com").userAgent("Mozilla").data("name", "steven").get();</code>
     * </li>
     * <li>
     * <code>Response response = JWebsoup.connect("http://example.com").cookie("auth", "token").post();</code>
     * </li>
     * </ul>
     * 
     * @param url URL to connect to. The protocol must be {@code http} or
     *            {@code https}.
     * @return the connection. You can add data, cookies, and headers; set the
     *         user-agent, referrer, method; and then execute.
     * 
     * @throws IOException
     */
    public static Connection connect(String url) {
        return ConnectionImpl.connect(url);
    }

    /**
     * Execute a request, which contains url, cookies, data and other info.
     * 
     * @param request request info
     * @param executor executor to request web
     * @return response info
     * @throws IOException
     */
    public static Response execute(Request request, Executor executor) throws IOException {
        return ConnectionImpl.create().request(request).executor(executor).execute();
    }

    /**
     * Execute a request, which contains url, cookies, data and other info.
     * 
     * @param request request info
     * @return response info
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return ConnectionImpl.create().request(request).execute();
    }

    /**
     * Execute a request and parse content by assinged parser.
     * 
     * @param request request info
     * @param executor executor to request web
     * @param parser webpage parser
     * @return parse result
     * 
     * @throws IOException
     */
    public static <T> T parse(Request request, Executor executor, Parser<T> parser) throws IOException {
        return execute(request, executor).parse(parser);
    }

    /**
     * Execute a request and parse content by assinged parser.
     * 
     * @param request request info
     * @param parser webpage parser
     * @return parse result
     * 
     * @throws IOException
     */
    public static <T> T parse(Request request, Parser<T> parser) throws IOException {
        return execute(request).parse(parser);
    }
}
