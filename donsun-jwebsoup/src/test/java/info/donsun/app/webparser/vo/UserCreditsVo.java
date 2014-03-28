/*
 * Copyright (c) 2013, ORZII and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.app.webparser.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;



/**
 *
 * @author Leo.du
 * @date 2013-12-12
 */
public class UserCreditsVo extends BaseVo {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("credits")
    private Double credits;
    
    @JsonProperty("modified")
    private Date modified;
    

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Double getCredits() {
        return this.credits;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getModified() {
        return this.modified;
    }
}

