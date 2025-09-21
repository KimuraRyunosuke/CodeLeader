package com.example.codeleader.controller;

import com.example.codeleader.dto.DiffRequest;
import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.service.AnalysisService;
import com.example.codeleader.service.ParseService;
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

    @Autowired
    private ParseService parseService;

    // =========================
    // Day1 解析API（既存）
    // =========================
    @PostMapping("/parse")
    public Map<String, Object> parse(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        if (code == null || code.isEmpty()) {
            return Map.of("status", "error", "message", "コードが空です");
        }
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
    // 新規：構造解析API（ParseService利用）
    // =========================
    @PostMapping("/parse-structure")
    public List<Map<String, Object>> parseStructure(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        if (code == null || code.isEmpty()) return List.of();
        return parseService.parseCode(code);
    }
}
