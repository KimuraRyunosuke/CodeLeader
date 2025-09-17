package com.example.codeleader.controller;

import com.example.codeleader.dto.DiffRequest;
import com.example.codeleader.dto.ParseRequest;
import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.model.MethodInfo;
import com.example.codeleader.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService service;

    // 解析API
    //@PostMapping("/parse")
    //public ResponseEntity<List<MethodInfo>> parse(@RequestBody ParseRequest req) {
    //    return ResponseEntity.ok(service.parse(req.getSource()));
    //}

    // 差分API
    @PostMapping("/diff")
    public ResponseEntity<List<MethodDiff>> diff(@RequestBody DiffRequest req) {
        return ResponseEntity.ok(service.diff(req.getOldSource(), req.getNewSource()));
    }
}
