package com.example.codeleader.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Access;

@Repository
public interface AccessRepository extends CrudRepository<Access, Long> {
    public List<Access> findByUserIdAndCodeId(String userId, long codeId);
    public List<Access> findByUserIdOrderByAccessedAtDesc(String userId);
}
