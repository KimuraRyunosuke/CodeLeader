package com.example.codeleader.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.FinishedReading;

@Repository
public interface FinishedReadingRepository extends CrudRepository<FinishedReading, Long> {
    public List<FinishedReading> findByUserIdAndCodeId(String userId, long codeId);

    public List<FinishedReading> findTop5ByUserIdOrderByFinishedAtDesc(String userId);
}
