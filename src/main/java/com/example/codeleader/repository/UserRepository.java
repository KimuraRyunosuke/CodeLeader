package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
}
