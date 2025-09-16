package com.example.codeleader.util;

import com.example.codeleader.model.MethodInfo;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

import java.util.*;

public class JavaParserUtil {

    public static List<MethodInfo> parseMethods(String source) {
        List<MethodInfo> out = new ArrayList<>();
        if (source == null || source.trim().isEmpty()) return out;
        CompilationUnit cu = StaticJavaParser.parse(source);
        // 全てのクラス/インターフェイスを走査
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
            String className = c.getNameAsString();
            List<MethodDeclaration> methods = c.getMethods();
            for (MethodDeclaration m : methods) {
                String methodName = m.getNameAsString();
                String signature = m.getDeclarationAsString(false, false, false);
                int begin = m.getRange().map(r -> r.begin.line).orElse(-1);
                int end = m.getRange().map(r -> r.end.line).orElse(-1);
                // 抜き出し：ソースの該当部分を行で切る（簡易）
                String methodSource = m.toString();
                MethodInfo mi = new MethodInfo(className, methodName, signature, begin, end, methodSource);
                out.add(mi);
            }
        });
        return out;
    }

    // ユーティリティ: キー（クラス名+signature）を作る
    public static String keyOf(MethodInfo m) {
        return m.getClassName() + "::" + m.getSignature();
    }
}
