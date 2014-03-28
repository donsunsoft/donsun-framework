/*
 * Copyright (c) 2013, ORZII and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.app.webparser.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;



/**
 *
 * @author Leo.du
 * @date 2013-12-12
 */
@JsonRootName("user")
public class UserVo extends BaseVo {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("salt")
    private String salt;
    
    @JsonProperty("roles")
    private String roles;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("credits_id")
    private Long creditsId;
    
    @JsonProperty("balance_id")
    private Long balanceId;
    
    @JsonProperty("online_id")
    private Long onlineId;
    
    @JsonProperty("grade_id")
    private Long gradeId;
    
    @JsonProperty("exp")
    private Long exp;
    
    @JsonProperty("banned")
    private Boolean banned;
    
    @JsonProperty("ban_reason")
    private String banReason;
    
    @JsonProperty("remark")
    private String remark;
    
    @JsonProperty("register_ip")
    private String registerIp;
    
    @JsonProperty("created")
    private Date created;
    
    @JsonProperty("modified")
    private Date modified;
    
    @JsonProperty("active")
    private Boolean active;
    

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return this.salt;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return this.roles;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
    public void setCreditsId(Long creditsId) {
        this.creditsId = creditsId;
    }

    public Long getCreditsId() {
        return this.creditsId;
    }
    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getBalanceId() {
        return this.balanceId;
    }
    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    public Long getOnlineId() {
        return this.onlineId;
    }
    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getGradeId() {
        return this.gradeId;
    }
    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getExp() {
        return this.exp;
    }
    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Boolean getBanned() {
        return this.banned;
    }
    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public String getBanReason() {
        return this.banReason;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }
    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getRegisterIp() {
        return this.registerIp;
    }
    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return this.created;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getModified() {
        return this.modified;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return this.active;
    }

    private UserOnlineVo userOnlineVo;

    public void setUserOnlineVo(UserOnlineVo userOnlineVo){
        this.userOnlineVo = userOnlineVo;
    }

    public UserOnlineVo getUserOnlineVo() {
        return userOnlineVo;
    }

    private UserGradeVo userGradeVo;

    public void setUserGradeVo(UserGradeVo userGradeVo){
        this.userGradeVo = userGradeVo;
    }

    public UserGradeVo getUserGradeVo() {
        return userGradeVo;
    }

    private UserBalanceVo userBalanceVo;

    public void setUserBalanceVo(UserBalanceVo userBalanceVo){
        this.userBalanceVo = userBalanceVo;
    }

    public UserBalanceVo getUserBalanceVo() {
        return userBalanceVo;
    }

    private UserCreditsVo userCreditsVo;

    public void setUserCreditsVo(UserCreditsVo userCreditsVo){
        this.userCreditsVo = userCreditsVo;
    }

    public UserCreditsVo getUserCreditsVo() {
        return userCreditsVo;
    }
}

