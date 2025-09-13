package com.example.codeleader.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CodeAnalysisService {

    // Javaソース文字列を受け取り、クラス・メソッド情報を返す
    public Map<String, Object> analyzeCode(String sourceCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            CompilationUnit cu = StaticJavaParser.parse(sourceCode);

            // クラス名一覧
            cu.getTypes().forEach(type -> {
                result.put(type.getNameAsString(), type.getMembers().toString());
            });

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        return result;
    }
}
