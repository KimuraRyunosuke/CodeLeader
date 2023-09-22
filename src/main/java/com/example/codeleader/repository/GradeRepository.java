package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.codeleader.entity.User;

public interface GradeRepository extends CrudRepository<User, Long> {
    
}
