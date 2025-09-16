package com.example.codeleader.model;

public class MethodInfo {
    private String className;
    private String methodName;
    private String signature;
    private int beginLine;
    private int endLine;
    private String source;

    // デフォルトコンストラクタ（必須：Jackson や JPA が利用する可能性あり）
    public MethodInfo() {}

    // 引数付きコンストラクタ（JavaParserUtil から呼ばれる）
    public MethodInfo(String className, String methodName, String signature,
                      int beginLine, int endLine, String source) {
        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.source = source;
    }

    // getter / setter
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

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
