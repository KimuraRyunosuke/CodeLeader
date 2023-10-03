package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.codeleader.entity.Grade;

public interface GradeRepository extends CrudRepository<Grade, Long> {
    
}
