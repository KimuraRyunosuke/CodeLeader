package com.example.codeleader.controller;

import com.example.codeleader.dto.DiffRequest;
import com.example.codeleader.dto.ParseRequest;
import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.model.MethodInfo;
import com.example.codeleader.model.TreeNode;
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

    /**
     * ソースコード解析 → 構造ツリー返却
     */
    @PostMapping("/parse")
    public ResponseEntity<List<TreeNode>> parse(@RequestBody ParseRequest req) {
        List<TreeNode> tree = service.parse(req.getSource());
        return ResponseEntity.ok(tree);
    }

    /**
     * ソースコード差分解析 → 差分リスト返却
     */
    @PostMapping("/diff")
    public ResponseEntity<List<MethodDiff>> diff(@RequestBody DiffRequest req) {
        List<MethodDiff> diffs = service.diff(req.getOldSource(), req.getNewSource());
        return ResponseEntity.ok(diffs);
    }
}
