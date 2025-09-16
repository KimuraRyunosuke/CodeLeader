// package com.example.codeleader.controller;
//
// import com.example.codeleader.service.CodeAnalysisService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.Map;
//
// @RestController
// @RequestMapping("/api/analysis")
// public class CodeAnalysisController {
//
//     @Autowired
//     private CodeAnalysisService analysisService;
//
//     @PostMapping("/parse")
//     public Map<String, Object> parseCode(@RequestBody Map<String, String> payload) {
//         String sourceCode = payload.get("code");
//         return analysisService.analyzeCode(sourceCode);
//     }
// }
