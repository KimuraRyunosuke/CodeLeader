package com.example.codeleader.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParseService {

    // 正規表現パターンを事前コンパイル（多少パフォーマンス向上）
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("\\bclass\\s+(\\w+)");
    private static final Pattern INTERFACE_NAME_PATTERN = Pattern.compile("\\binterface\\s+(\\w+)");
    private static final Pattern ENUM_NAME_PATTERN = Pattern.compile("\\benum\\s+(\\w+)");
    private static final Pattern FIELD_PATTERN = Pattern.compile(".*\\b(public|protected|private|static|final)\\b.*;\\s*$");
    private static final Pattern VAR_PATTERN = Pattern.compile("^[\\w<>\\[\\],]+\\s+\\w+\\s*(=|;).*"); // ローカル変数など
    private static final Pattern CONST_PATTERN = Pattern.compile(".*\\bstatic\\b.*\\bfinal\\b.*;\\s*$");

    public List<Map<String, Object>> parseCode(String source) {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] lines = source.split("\n", -1); // 最後の空行も取り扱う

        String currentClassName = null;
        boolean inBlockComment = false;

        for (int i = 0; i < lines.length; i++) {
            String rawLine = lines[i];
            String line = rawLine.trim();

            // ブロックコメント中ならコメントとして扱い、終了を検出
            if (inBlockComment) {
                result.add(makeEntry(i + 1, rawLine, "コメント"));
                if (line.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }

            // 空行
            if (line.isEmpty()) {
                result.add(makeEntry(i + 1, rawLine, "空行"));
                continue;
            }

            // 行コメント開始
            if (line.startsWith("//")) {
                result.add(makeEntry(i + 1, rawLine, "コメント"));
                continue;
            }

            // ブロックコメント開始
            if (line.startsWith("/*")) {
                result.add(makeEntry(i + 1, rawLine, "コメント"));
                if (!line.contains("*/")) {
                    inBlockComment = true;
                }
                continue;
            }

            String type = null;

            // パッケージ / import / annotation
            if (line.startsWith("package ")) {
                type = "パッケージ宣言";
            } else if (line.startsWith("import ")) {
                type = "インポート宣言";
            } else if (line.startsWith("@")) {
                type = "アノテーション";
            }
            // クラス/インターフェース/enum 判定（先にクラス系を取る）
            else if (line.matches(".*\\babstract\\b.*\\bclass\\b.*")) {
                type = "抽象クラス定義";
                String cls = extractClassName(line);
                if (cls != null) currentClassName = cls;
            } else if (line.matches(".*\\bfinal\\b.*\\bclass\\b.*")) {
                type = "finalクラス定義";
                String cls = extractClassName(line);
                if (cls != null) currentClassName = cls;
            } else if (line.contains(" class ")) {
                type = "クラス定義";
                String cls = extractClassName(line);
                if (cls != null) currentClassName = cls;
            } else if (line.contains(" interface ")) {
                type = "インターフェース定義";
                Matcher m = INTERFACE_NAME_PATTERN.matcher(line);
                if (m.find()) {
                    // currentClassName を上書きしない（interface はクラス名判定には使わない）
                }
            } else if (line.contains(" enum ")) {
                type = "列挙型定義";
                Matcher m = ENUM_NAME_PATTERN.matcher(line);
                if (m.find()) {
                    // 省略
                }
            }
            // 制御構文類はメソッド判定より**先**に
            else if (line.matches("^else\\s+if\\b.*")) {
                type = "条件分岐 (else if)";
            } else if (line.matches("^if\\b.*")) {
                type = "条件分岐 (if)";
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
            } else if (line.matches("^(case\\b.*|default:).*")) {
                type = "条件分岐 (case/default)";
            } else if (line.matches("^try\\b.*")) {
                type = "例外処理 (try)";
            } else if (line.matches("^catch\\b.*")) {
                type = "例外処理 (catch)";
            } else if (line.matches("^finally\\b.*")) {
                type = "例外処理 (finally)";
            } else if (line.startsWith("return ")) {
                type = "return文";
            } else if (line.contains("System.out.println")) {
                type = "標準出力";
            }
            // 定数・フィールド・変数判定（;で終わるもの）
            else if (CONST_PATTERN.matcher(line).matches()) {
                type = "定数定義";
            } else if (FIELD_PATTERN.matcher(line).matches()) {
                type = "フィールド定義";
            } else if (VAR_PATTERN.matcher(line).matches()) {
                // public/private修飾子が無い場合でもローカル変数や型付き宣言と判断
                type = "変数定義";
            }
            // メソッド／コンストラクタ判定（括弧があり、終端がセミコロンでないもの）
            else if (line.contains("(") && line.contains(")") && !line.endsWith(";")) {
                // コンストラクタか判定（クラス名と一致）
                if (currentClassName != null) {
                    // 単語境界でコンストラクタ名が含まれているかをチェック
                    String ctorRegex = "^.*\\b" + Pattern.quote(currentClassName) + "\\s*\\(.*";
                    if (line.matches(ctorRegex)) {
                        type = "コンストラクタ定義";
                    }
                }
                if (type == null) {
                    type = "メソッド定義";
                }
            } else {
                // ここまでに当てはまらなければ「その他」
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
        Matcher m = CLASS_NAME_PATTERN.matcher(line);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
