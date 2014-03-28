package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Parse web by jackson
 * 
 * @author Steven
 * 
 * @param <T>
 */
public class XmlNodeParser extends XmlParser<JsonNode> {

    /**
     * Create a parser
     * 
     * @return XmlNodeParser
     */
    public static XmlNodeParser create() {
        return new XmlNodeParser();
    }

    @Override
    public JsonNode parse(Request request, Response response) throws IOException {
        return mapper().readTree(response.body());
    }

}
