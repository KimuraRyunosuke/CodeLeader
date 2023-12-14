package com.example.codeleader.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.codeleader.entity.Memo;
import java.util.List;


@Repository
public interface MemoRepository extends CrudRepository<Memo, Long> {
    List<Memo> findByUserIdAndCodeId(long userId, long codeId);
}
