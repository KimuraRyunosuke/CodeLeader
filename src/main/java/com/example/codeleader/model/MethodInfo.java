package com.example.codeleader.model;

public class MethodInfo {
    private String className;
    private String methodName;
    private String signature;
    private int beginLine;
    private int endLine;
    private String source;

    // constructors, getters, setters
    public MethodInfo() {}
    public MethodInfo(String className, String methodName, String signature, int beginLine, int endLine, String source) {
        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.source = source;
    }
    // getters / setters omitted for brevity (generate via IDE)
}
