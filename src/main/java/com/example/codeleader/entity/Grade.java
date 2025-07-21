package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Grade {
    
    @Id
    private String id;

    private Integer value;

}
