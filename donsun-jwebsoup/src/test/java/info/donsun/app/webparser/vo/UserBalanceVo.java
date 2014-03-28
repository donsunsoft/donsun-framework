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
public class UserBalanceVo extends BaseVo {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("balance")
    private Double balance;
    
    @JsonProperty("freeze_banance")
    private Double freezeBanance;
    
    @JsonProperty("modified")
    private Date modified;
    

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return this.balance;
    }
    public void setFreezeBanance(Double freezeBanance) {
        this.freezeBanance = freezeBanance;
    }

    public Double getFreezeBanance() {
        return this.freezeBanance;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getModified() {
        return this.modified;
    }
}

