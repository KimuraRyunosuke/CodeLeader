package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Access;

@Repository
public interface AccessRepository extends CrudRepository<Access, Long> {
    
}
