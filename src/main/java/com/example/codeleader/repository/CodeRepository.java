package com.example.codeleader.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Code;

@Repository
public interface CodeRepository extends CrudRepository<Code, Long> {
    public List<Code> findByPostId(long postId);

    public List<Code> findAllByOrderByReaderCountDesc();

    public List<Code> findTop5ByOrderByReaderCountDesc();
}
