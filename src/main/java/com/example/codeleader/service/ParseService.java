package com.example.codeleader.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParseService {

    public List<Map<String, Object>> parseCode(String source) {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] lines = source.split("\n");

        // クラス名（constructor 判定用に使う）
        String currentClassName = null;

        for (int i = 0; i < lines.length; i++) {
            String rawLine = lines[i];
            String line = rawLine.trim();
            if (line.isEmpty()) {
                result.add(makeEntry(i + 1, rawLine, "blank"));
                continue;
            }

            String type;
            if (line.startsWith("package ")) {
                type = "package";
            } else if (line.startsWith("import ")) {
                type = "import";
            } else if (line.startsWith("@")) {
                type = "annotation";
            } else if (line.contains(" class ")) {
                type = "class";
                currentClassName = extractClassName(line);
            } else if (line.contains(" interface ")) {
                type = "interface";
            } else if (line.contains(" enum ")) {
                type = "enum";
            } else if (line.matches(".*\\(.*\\).*\\{?")) {
                // constructor 判定
                if (currentClassName != null && line.contains(currentClassName + "(")) {
                    type = "constructor";
                } else {
                    type = "method";
                }
            } else if (line.matches(".*(private|public|protected).+;")) {
                type = "field";
            } else if (line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) {
                type = "comment";
            } else {
                type = "other";
            }

            result.add(makeEntry(i + 1, rawLine, type));
        }

        return result;
    }

    private Map<String, Object> makeEntry(int line, String text, String type) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("line", line);
        entry.put("text", text);
        entry.put("type", type);
        return entry;
    }

    private String extractClassName(String line) {
        // 例: "public class User {" → "User"
        String[] parts = line.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("class") && i + 1 < parts.length) {
                return parts[i + 1].replace("{", "");
            }
        }
        return null;
    }
}
