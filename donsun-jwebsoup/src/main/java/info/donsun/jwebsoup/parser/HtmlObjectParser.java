package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

public abstract class HtmlObjectParser<T> implements Parser<T> {

    @Override
    public T parse(Request request, Response response) throws IOException {
        return parse(request, response,
                DataUtil.load(new ByteArrayInputStream(response.bodyAsBytes()), response.charset(), request.url().toExternalForm(), org.jsoup.parser.Parser.htmlParser()));
    }

    public abstract T parse(Request request, Response response, Document document) throws IOException;

}
