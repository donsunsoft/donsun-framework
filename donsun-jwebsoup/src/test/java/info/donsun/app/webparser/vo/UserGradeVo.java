/*
 * Copyright (c) 2013, ORZII and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.app.webparser.vo;

import com.fasterxml.jackson.annotation.JsonProperty;




/**
 *
 * @author Leo.du
 * @date 2013-12-12
 */
public class UserGradeVo extends BaseVo {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("title")
    private String title;
    
    @JsonProperty("weight")
    private Double weight;
    
    @JsonProperty("credits_coefficient")
    private Double creditsCoefficient;
    
    @JsonProperty("next_grade_id")
    private Long nextGradeId;
    
    @JsonProperty("upgrade_exp")
    private Long upgradeExp;
    

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeight() {
        return this.weight;
    }
    public void setCreditsCoefficient(Double creditsCoefficient) {
        this.creditsCoefficient = creditsCoefficient;
    }

    public Double getCreditsCoefficient() {
        return this.creditsCoefficient;
    }
    public void setNextGradeId(Long nextGradeId) {
        this.nextGradeId = nextGradeId;
    }

    public Long getNextGradeId() {
        return this.nextGradeId;
    }
    public void setUpgradeExp(Long upgradeExp) {
        this.upgradeExp = upgradeExp;
    }

    public Long getUpgradeExp() {
        return this.upgradeExp;
    }

    private UserGradeVo userGradeVo;

    public void setUserGradeVo(UserGradeVo userGradeVo){
        this.userGradeVo = userGradeVo;
    }

    public UserGradeVo getUserGradeVo() {
        return userGradeVo;
    }
}

