package com.example.codeleader.service;

import jakarta.annotation.PostConstruct; // ← Spring Boot 3 以降はこちら
import org.springframework.stereotype.Service;          // ← @Service を解決
import com.example.codeleader.model.NodeInfo;       // ← NodeInfo モデルを利用
import java.util.HashMap;                           // ← HashMap を利用
import java.util.Map;

@Service
public class CodeService {

    private final Map<String, NodeInfo> nodeCache = new HashMap<>();

    @PostConstruct
    public void init() {
        NodeInfo greetNode = new NodeInfo();
        greetNode.setName("greet");
        greetNode.setType("method");
        greetNode.setStartLine(10);
        greetNode.setEndLine(15);
        greetNode.setSource("void greet(String name) {\n    System.out.println(\"Hello, \" + name);\n}");
        greetNode.setDiffStatus("modified");

        registerNode("Hello.java:greet", greetNode);
    }

    // ノードを登録（差分解析などで使う）
    public void registerNode(String id, NodeInfo info) {
        nodeCache.put(id, info);
    }

    // ノード情報を取得
    public NodeInfo getNodeInfoById(String id) {
        return nodeCache.get(id); // 登録がなければ null → Controller 側で404対応
    }
}
