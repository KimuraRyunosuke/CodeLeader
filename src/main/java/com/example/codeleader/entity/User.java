package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    
    @Id
    private String id;

    private String name;
    private Integer lv = 1;
    private Integer exp = 0;
    private Integer point = 0;
    private String grade = "D";

}