package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.codeleader.entity.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
    
}
