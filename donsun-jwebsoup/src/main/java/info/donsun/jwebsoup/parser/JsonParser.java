package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.util.Utils;
import info.donsun.jwebsoup.util.Validate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Parse web by jackson
 * 
 * @author Steven
 * 
 * @param <T>
 */
public abstract class JsonParser<T> implements Parser<T> {

    /**
     * ObjectMapper
     */
    private ObjectMapper mapper;

    /**
     * Set ObjectMapper
     * 
     * @param mapper new ObjectMapper
     * @return this JsonParser, for chaining
     */
    public JsonParser<T> mapper(ObjectMapper mapper) {
        Validate.notNull(mapper, "ObjectMapper must not be null");
        this.mapper = mapper;
        return this;
    }

    /**
     * Get ObjectMapper
     * 
     * @return the ObjectMapper
     */
    protected ObjectMapper mapper() {
        if (mapper == null) {
            mapper = Utils.getJsonMapper();
        }
        return mapper;
    }

}
