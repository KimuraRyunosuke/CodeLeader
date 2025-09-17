package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // ★ これを追加
import lombok.Data;

@Data
@Entity
@Table(name = "users") // ← user ではなく users にする
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private Integer lv = 1;
    private Integer exp = 0;
    private Integer point = 0;
    private String grade = "Bronze";

}