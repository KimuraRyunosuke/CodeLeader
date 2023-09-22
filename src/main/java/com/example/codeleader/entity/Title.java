package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;
    private Integer titleListId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId){
        this.userId = userId;
    }

    public Integer getTitleListId() {
        return titleListId;
    }
    public void setTitleListId(Integer titleListId){
        this.titleListId = titleListId;
    }
}
