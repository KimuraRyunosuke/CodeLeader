package com.example.codeleader.model;

public class MethodDiff {
    private String className;
    private String methodName;
    private String signature;
    private int beginLine;
    private int endLine;
    private String status;     // added / removed / unchanged / modified
    private String oldSource;
    private String newSource;

    // --- getters and setters ---
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public int getBeginLine() { return beginLine; }
    public void setBeginLine(int beginLine) { this.beginLine = beginLine; }

    public int getEndLine() { return endLine; }
    public void setEndLine(int endLine) { this.endLine = endLine; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOldSource() { return oldSource; }
    public void setOldSource(String oldSource) { this.oldSource = oldSource; }

    public String getNewSource() { return newSource; }
    public void setNewSource(String newSource) { this.newSource = newSource; }
}
