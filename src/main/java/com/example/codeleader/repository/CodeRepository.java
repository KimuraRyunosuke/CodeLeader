package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.codeleader.entity.Code;

public interface CodeRepository extends CrudRepository<Code, Long> {
    
}
