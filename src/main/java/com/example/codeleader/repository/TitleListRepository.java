package com.example.codeleader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.TitleList;

@Repository
public interface TitleListRepository extends CrudRepository<TitleList, Long> {
    
}
