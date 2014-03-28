/*
 * Copyright (c) 2013, ORZII and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.app.webparser.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;




/**
 *
 * @author Leo.du
 * @date 2013-12-12
 */
@JsonRootName("app_option")
public class AppOptionVo extends BaseVo {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("option_key")
    private String optionKey;
    
    @JsonProperty("option_value")
    private String optionValue;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("active")
    private Boolean active;
    

    public void setOptionKey(String optionKey) {
        this.optionKey = optionKey;
    }

    public String getOptionKey() {
        return this.optionKey;
    }
    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionValue() {
        return this.optionValue;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return this.active;
    }
}

