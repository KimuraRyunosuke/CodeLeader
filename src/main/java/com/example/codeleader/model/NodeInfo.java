package com.example.codeleader.model;

public class NodeInfo {
    private String name;
    private String type;
    private int startLine;
    private int endLine;
    private String source;
    private String diffStatus;

    // Getter & Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getStartLine() { return startLine; }
    public void setStartLine(int startLine) { this.startLine = startLine; }

    public int getEndLine() { return endLine; }
    public void setEndLine(int endLine) { this.endLine = endLine; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDiffStatus() { return diffStatus; }
    public void setDiffStatus(String diffStatus) { this.diffStatus = diffStatus; }
}
