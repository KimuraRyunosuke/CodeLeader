package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.FinishedReading;

@Repository
public interface FinishedReadingRepository extends CrudRepository<FinishedReading, Long> {
    
}
