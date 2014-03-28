/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.activator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 资源
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-10-14
 * 
 */

@JacksonXmlRootElement(localName = "resource")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

    @JacksonXmlProperty(isAttribute = true)
    private String location;

    @JacksonXmlProperty(isAttribute = true)
    private String checksum;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

}
