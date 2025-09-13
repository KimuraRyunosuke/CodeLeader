package com.example.codeleader.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    // Node情報を返すダミーAPI（まずは固定データでOK）
    @GetMapping("/node/{id}")
    public ResponseEntity<NodeInfo> getNodeInfo(@PathVariable String id) {
        // ここではダミーとして固定レスポンスを返す
        NodeInfo info = new NodeInfo();
        info.setName("greet");
        info.setType("method");
        info.setStartLine(10);
        info.setEndLine(15);
        info.setSource("void greet(String name) {\n    System.out.println(\"Hello, \" + name);\n}");
        info.setDiffStatus("modified");

        return ResponseEntity.ok(info);
    }
}
