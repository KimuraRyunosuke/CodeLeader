package com.example.codeleader;

import java.sql.Timestamp;
import java.util.List;


import com.example.codeleader.entity.Code;

import lombok.Data;

@Data
public class CodeSet {
    private long postId;
    private String title;
    private String comment;
    private String lang;
    private Timestamp postedAt;
    private List<Code> codeList;

}
