package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Grade;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {
    
}
