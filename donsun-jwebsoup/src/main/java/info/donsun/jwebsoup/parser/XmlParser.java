package info.donsun.jwebsoup.parser;

import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.util.Utils;
import info.donsun.jwebsoup.util.Validate;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Parse web by jackson
 * 
 * @author Steven
 * 
 * @param <T>
 */
public abstract class XmlParser<T> implements Parser<T> {

    /**
     * XmlMapper
     */
    private XmlMapper mapper;

    /**
     * Set XmlMapper
     * 
     * @param mapper new XmlMapper
     * @return this JsonParser, for chaining
     */
    public XmlParser<T> mapper(XmlMapper mapper) {
        Validate.notNull(mapper, "XmlMapper must not be null");
        this.mapper = mapper;
        return this;
    }

    /**
     * Get XmlMapper
     * 
     * @return the XmlMapper
     */
    protected XmlMapper mapper() {
        if (mapper == null) {
            mapper = Utils.getXmlMapper();
        }
        return mapper;
    }

}
