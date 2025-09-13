package com.example.codeleader.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.codeleader.service.CodeService;
import com.example.codeleader.model.NodeInfo;  // ← ここを model.NodeInfo に統一！

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    private CodeService codeService;

    /**
     * ノードIDからノード情報を取得
     * 例: GET /api/code/node/Hello.java:MyClass.greet
     */
    @GetMapping("/node/{id}")
    public NodeInfo getNodeInfo(@PathVariable String id) {
        return codeService.getNodeInfoById(id);
    }
}
