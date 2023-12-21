package com.example.codeleader.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Bookmark;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
    public List<Bookmark> findByUserIdAndCodeId(String userId, long codeId);
    public List<Bookmark> findByUserIdOrderByIdDesc(String userId);
}
