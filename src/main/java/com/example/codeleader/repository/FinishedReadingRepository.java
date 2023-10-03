package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.codeleader.entity.FinishedReading;

public interface FinishedReadingRepository extends CrudRepository<FinishedReading, Long> {
    
}
