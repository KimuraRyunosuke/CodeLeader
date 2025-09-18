package com.example.codeleader.controller;

import com.example.codeleader.dto.DiffRequest;
import com.example.codeleader.dto.ParseRequest;
import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*") // どのオリジンからもアクセス可能
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService service;

    // =========================
    // Day1 解析API
    // =========================
    @PostMapping("/parse")
    public ResponseEntity<Object> parse(@RequestBody ParseRequest req) {
        String code = req.getSource();
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "コードが空です")
            );
        }

        // サンプル解析：クラス名抽出（簡易）
        String className = "Unknown";
        if (code.contains("class ")) {
            int start = code.indexOf("class ") + 6;
            int end = code.indexOf(" ", start);
            if (end == -1) end = code.indexOf("{", start);
            className = code.substring(start, end).trim();
        }

        // 解析結果を返す（サンプル）
        return ResponseEntity.ok(Map.of(className, "[解析済みメソッドサンプル]"));
    }

    // =========================
    // 差分API（既存）
    // =========================
    @PostMapping("/diff")
    public ResponseEntity<List<MethodDiff>> diff(@RequestBody DiffRequest req) {
        return ResponseEntity.ok(service.diff(req.getOldSource(), req.getNewSource()));
    }
}
