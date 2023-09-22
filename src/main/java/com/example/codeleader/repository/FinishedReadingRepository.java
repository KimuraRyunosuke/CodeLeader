package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.codeleader.entity.User;

public interface FinishedReadingRepository extends CrudRepository<User, Long> {
    
}
