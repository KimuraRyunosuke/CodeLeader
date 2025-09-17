package com.example.codeleader.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @PostMapping("/parse")
    public Map<String, Object> parseCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        // ここで解析処理を呼ぶ（まだなければ仮で返す）
        return Map.of("parsedCode", code);
    }
}
