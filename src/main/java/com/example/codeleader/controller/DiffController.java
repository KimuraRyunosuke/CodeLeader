package com.example.codeleader.controller;

import com.example.codeleader.model.DiffRequest;
import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.service.CodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final CodeService service;

    public AnalysisController(CodeService service) {
        this.service = service;
    }

    // ✅ Day1: 解析
    @PostMapping("/parse")
    public ResponseEntity<?> parse(@RequestBody Map<String, String> payload) {
        String code = payload.getOrDefault("code", "");
        return ResponseEntity.ok(service.parse(code));
    }

    // ✅ Day2: 差分解析
    @PostMapping("/diff")
    public ResponseEntity<List<MethodDiff>> diff(@RequestBody DiffRequest req) {
        return ResponseEntity.ok(service.diff(req.getOldSource(), req.getNewSource()));
    }
}
