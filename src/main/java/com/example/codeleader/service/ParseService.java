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
                result.add(makeEntry(i + 1, rawLine, "空行"));
                continue;
            }

            String type;
            if (line.startsWith("package ")) {
                type = "パッケージ宣言";
            } else if (line.startsWith("import ")) {
                type = "インポート宣言";
            } else if (line.startsWith("@")) {
                type = "アノテーション";
            } else if (line.contains(" abstract class ")) {
                type = "抽象クラス定義";
                currentClassName = extractClassName(line);
            } else if (line.contains(" final class ")) {
                type = "finalクラス定義";
                currentClassName = extractClassName(line);
            } else if (line.contains(" class ")) {
                type = "クラス定義";
                currentClassName = extractClassName(line);
            } else if (line.contains(" interface ")) {
                type = "インターフェース定義";
            } else if (line.contains(" enum ")) {
                type = "列挙型定義";
            } else if (line.matches(".*\\(.*\\).*\\{?")) {
                // constructor 判定
                if (currentClassName != null && line.contains(currentClassName + "(")) {
                    type = "コンストラクタ定義";
                } else {
                    type = "メソッド定義";
                }
            } else if (line.matches(".*static\\s+final.+;")) {
                type = "定数定義";
            } else if (line.matches(".*(private|public|protected).+;")) {
                type = "フィールド定義";
            } else if (line.matches("^if\\b.*")) {
                type = "条件分岐 (if)";
            } else if (line.matches("^else\\s+if\\b.*")) {
                type = "条件分岐 (else if)";
            } else if (line.matches("^else\\b.*")) {
                type = "条件分岐 (else)";
            } else if (line.matches("^for\\b.*")) {
                type = "ループ (for)";
            } else if (line.matches("^while\\b.*")) {
                type = "ループ (while)";
            } else if (line.matches("^do\\b.*")) {
                type = "ループ (do-while)";
            } else if (line.matches("^switch\\b.*")) {
                type = "条件分岐 (switch)";
            } else if (line.matches("^(case\\b.*|default:)")) {
                type = "条件分岐 (case/default)";
            } else if (line.startsWith("try")) {
                type = "例外処理 (try)";
            } else if (line.startsWith("catch")) {
                type = "例外処理 (catch)";
            } else if (line.startsWith("finally")) {
                type = "例外処理 (finally)";
            } else if (line.startsWith("return ")) {
                type = "return文";
            } else if (line.contains("System.out.println")) {
                type = "標準出力";
            } else if (line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) {
                type = "コメント";
            } else {
                type = "その他";
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
        String[] parts = line.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("class") && i + 1 < parts.length) {
                return parts[i + 1].replace("{", "");
            }
        }
        return null;
    }
}

