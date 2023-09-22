package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Rank {
    
    @Id
    private String id;

    private Integer value;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
}
