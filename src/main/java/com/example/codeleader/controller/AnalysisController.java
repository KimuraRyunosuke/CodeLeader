package com.example.codeleader.controller;

import com.example.codeleader.dto.DiffRequest;
import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService service;

    // =========================
    // Day1 解析API（既存）
    // =========================
    @PostMapping("/parse")
    public Map<String, Object> parse(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        if (code == null || code.isEmpty()) {
            return Map.of("status", "error", "message", "コードが空です");
        }

        // サンプル解析：クラス名抽出など簡易返却
        return Map.of("Hello", "[解析済みメソッドサンプル]");
    }

    // =========================
    // 差分API（既存）
    // =========================
    @PostMapping("/diff")
    public ResponseEntity<List<MethodDiff>> diff(@RequestBody DiffRequest req) {
        return ResponseEntity.ok(service.diff(req.getOldSource(), req.getNewSource()));
    }

    // =========================
    // 新規：構造解析API（Javaのみ）
    // =========================
    @PostMapping("/parse-structure")
    public List<Map<String, Object>> parseStructure(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        List<Map<String, Object>> result = new ArrayList<>();
        if (code == null || code.isEmpty()) return result;

        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue; // 不要行はスキップ

            Map<String, Object> node = new HashMap<>();
            if (line.startsWith("if(") || line.startsWith("if (")) {
                node.put("line", i + 1);
                node.put("type", "if");
                node.put("text", lines[i]);
            } else if (line.startsWith("for(") || line.startsWith("for (")) {
                node.put("line", i + 1);
                node.put("type", "for");
                node.put("text", lines[i]);
            } else if (line.startsWith("class ")) {
                node.put("line", i + 1);
                node.put("type", "class");
                node.put("text", lines[i]);
            } else if (line.startsWith("public ") || line.startsWith("private ") || line.startsWith("protected ")) {
                node.put("line", i + 1);
                node.put("type", "method");
                node.put("text", lines[i]);
            }

            if (!node.isEmpty()) result.add(node); // JSONに追加
        }

        return result;
    }
}
