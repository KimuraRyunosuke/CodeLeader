package com.example.codeleader.service;

import com.example.codeleader.model.TreeNode;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class TreeService {

    public List<TreeNode> getTree() {
        // ここでは静的サンプル JSON を返す
        return Arrays.asList(
                new TreeNode(
                        "file1",
                        "Hello.java",
                        "file",
                        false,
                        Arrays.asList(
                                new TreeNode(
                                        "class1",
                                        "Hello",
                                        "class",
                                        true,
                                        Arrays.asList(
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
