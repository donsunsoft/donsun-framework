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
public class UserOnlineVo extends BaseVo {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("total_online")
    private Double totalOnline;
    
    @JsonProperty("daily_online")
    private Double dailyOnline;
    
    @JsonProperty("last_ip")
    private String lastIp;
    
    @JsonProperty("last_login")
    private Date lastLogin;
    
    @JsonProperty("last_beat")
    private Date lastBeat;
    
    @JsonProperty("secret_key")
    private String secretKey;
    

    public void setTotalOnline(Double totalOnline) {
        this.totalOnline = totalOnline;
    }

    public Double getTotalOnline() {
        return this.totalOnline;
    }
    public void setDailyOnline(Double dailyOnline) {
        this.dailyOnline = dailyOnline;
    }

    public Double getDailyOnline() {
        return this.dailyOnline;
    }
    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getLastIp() {
        return this.lastIp;
    }
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }
    public void setLastBeat(Date lastBeat) {
        this.lastBeat = lastBeat;
    }

    public Date getLastBeat() {
        return this.lastBeat;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }
}

