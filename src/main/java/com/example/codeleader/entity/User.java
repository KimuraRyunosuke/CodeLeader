package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private Integer lv = 0;
    private Integer exp = 0;
    private Integer point = 0;
    private String rank = "Bronze";

    public long getId() {
        return id;
    }
    public void setId(long anId) {
        this.id = anId;
    }

    public String getName() {
        return name;
    }
    public void setName(String aName) {
        this.name = aName;
    }

    public Integer getLv() {
        return lv;
    }
    public void setLv(Integer aLv) {
        this.lv = aLv;
    }

    public Integer getExp() {
        return exp;
    }
    public void setExp(Integer anExp) {
        this.exp = anExp;
    }

    public Integer getPoint() {
        return point;
    }
    public void setPoint(Integer aPoint) {
        this.point = aPoint;
    }

    public String getRank() {
        return rank;
    }
    public void setRank(String aRank) {
        this.rank = aRank;
    }

}