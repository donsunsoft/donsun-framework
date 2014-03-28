package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

/**
 * Parse web by jsoup
 * 
 * @author Steven
 * 
 */
public class HtmlDocumentParser implements Parser<Document> {

    /**
     * Create a parser
     * 
     * @return HtmlDocumentParser
     */
    public static HtmlDocumentParser create() {
        return new HtmlDocumentParser();
    }

    @Override
    public Document parse(Request request, Response response) throws IOException {
        return DataUtil.load(new ByteArrayInputStream(response.bodyAsBytes()), response.charset(), request.url().toExternalForm(), org.jsoup.parser.Parser.htmlParser());
    }

}
