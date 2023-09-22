package com.example.codeleader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Code {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long postId;
    private String url;
    private Integer loc;
    private Integer point;
    private Integer readerCount;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getPostId() {
        return postId;
    }
    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLoc() {
        return loc;
    }
    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public Integer getPoint() {
        return point;
    }
    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getReaderCount() {
        return readerCount;
    }
    public void setReaderCount(Integer readerCount) {
        this.readerCount = readerCount;
    }

}