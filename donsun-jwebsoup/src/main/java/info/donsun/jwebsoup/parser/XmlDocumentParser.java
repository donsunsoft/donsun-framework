package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

public class XmlDocumentParser implements Parser<Document> {

    public static XmlDocumentParser create() {
        return new XmlDocumentParser();
    }

    @Override
    public Document parse(Request request, Response response) throws IOException {
        return DataUtil.load(new ByteArrayInputStream(response.bodyAsBytes()), response.charset(), request.url().toExternalForm(),
                org.jsoup.parser.Parser.xmlParser());
    }

}
