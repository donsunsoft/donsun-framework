package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.util.Utils;

import java.io.IOException;

/**
 * Parse web by jackson
 * 
 * @author Steven
 * 
 * @param <T>
 */
public abstract class XmlObjectParser<T> extends XmlParser<T> {

    @Override
    public T parse(Request request, Response response) throws IOException {
        Class<T> valueType = Utils.getClassGenricType(getClass());

        return mapper().readValue(response.bodyAsBytes(), valueType);
    }

}
