package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.codeleader.entity.Title;

public interface TitleRepository extends CrudRepository<Title, Long> {
    
}
