package com.example.codeleader.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @GetMapping("/{id}")
    public NodeDetail getNodeDetail(@PathVariable String id) {
        // TODO: 実際は解析結果からソースや差分を返す
        if (id.equals("Hello.java")) {
            return new NodeDetail("Hello.java",
                    "public class Hello {\n    void greet() {\n        System.out.println(\"Hello\");\n    }\n}",
                    "メソッド greet() が変更されました");
        } else if (id.equals("Utils.java")) {
            return new NodeDetail("Utils.java",
                    "public class Utils {\n    static int add(int a, int b) {\n        return a + b;\n    }\n}",
                    "");
        }
        return new NodeDetail(id, "ソース不明", "");
    }

    static class NodeDetail {
        public String name;
        public String source;
        public String diff;
        public NodeDetail(String name, String source, String diff) {
            this.name = name;
            this.source = source;
            this.diff = diff;
        }
    }
}
