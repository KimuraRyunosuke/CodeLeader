package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Title;

@Repository
public interface TitleRepository extends CrudRepository<Title, Long> {
    
}
