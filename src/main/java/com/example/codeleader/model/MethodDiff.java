package com.example.codeleader.model;

public class MethodDiff {
    private String className;
    private String methodName;
    private String signature;
    private int beginLine;
    private int endLine;
    private String status; // "added","removed","modified","unchanged"
    private String oldSource;
    private String newSource;

    public MethodDiff() {}
    // constructor / getters / setters
}
