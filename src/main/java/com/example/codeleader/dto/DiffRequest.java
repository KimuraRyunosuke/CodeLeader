package com.example.codeleader.dto;

public class DiffRequest {
    private String oldSource;
    private String newSource;

    public String getOldSource() {
        return oldSource;
    }

    public void setOldSource(String oldSource) {
        this.oldSource = oldSource;
    }

    public String getNewSource() {
        return newSource;
    }

    public void setNewSource(String newSource) {
        this.newSource = newSource;
    }
}
