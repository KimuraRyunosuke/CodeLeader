package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.codeleader.entity.Bookmark;

public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
    
}
