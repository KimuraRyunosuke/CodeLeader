package com.example.codeleader.controller;

import com.example.codeleader.model.TreeNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @GetMapping("/tree")
    public List<TreeNode> getTree() {
        // 静的 JSON サンプル
        return List.of(
                new TreeNode(
                        "file1",
                        "Hello.java",
                        "file",
                        false,
                        List.of(
                                new TreeNode(
                                        "class1",
                                        "Hello",
                                        "class",
                                        true,
                                        List.of(
                                                new TreeNode(
                                                        "method1",
                                                        "greet(String name)",
                                                        "method",
                                                        false,
                                                        null
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
