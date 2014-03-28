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
 */
public class JsonNodeParser extends JsonParser<JsonNode> {

    /**
     * Create a parser
     * 
     * @return JsonNodeParser
     */
    public static JsonNodeParser create() {
        return new JsonNodeParser();
    }

    @Override
    public JsonNode parse(Request request, Response response) throws IOException {
        return mapper().readTree(response.body());
    }

}
