package com.example.codeleader.controller;

import com.example.codeleader.dto.DiffRequest;
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
    public Map<String, Object> parse(@RequestBody Map<String, String> request) {
        String code = request.get("code"); // JSONのkeyと一致
        if (code == null || code.isEmpty()) {
            return Map.of("status", "error", "message", "コードが空です");
        }

        // サンプル解析：クラス名抽出などは簡易でサンプル返却
        return Map.of("Hello", "[解析済みメソッドサンプル]");
    }

    // =========================
    // 差分API（既存）
    // =========================
    @PostMapping("/diff")
    public ResponseEntity<List<MethodDiff>> diff(@RequestBody DiffRequest req) {
        return ResponseEntity.ok(service.diff(req.getOldSource(), req.getNewSource()));
    }
}
