package com.example.codeleader.service;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiffService {

    public List<Map<String, Object>> getDiff(String oldSource, String newSource) {
        List<String> original = Arrays.asList(oldSource.split("\n"));
        List<String> revised = Arrays.asList(newSource.split("\n"));

        Patch<String> patch = DiffUtils.diff(original, revised);

        List<Map<String, Object>> diffs = new ArrayList<>();

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            Map<String, Object> diffEntry = new HashMap<>();
            diffEntry.put("変更種別", localizeDiffType(delta.getType().toString())); // 日本語化
            diffEntry.put("旧コードの内容", delta.getSource().getLines());          // source
            diffEntry.put("新コードの内容", delta.getTarget().getLines());          // target
            diffEntry.put("旧コードの位置", delta.getSource().getPosition());      // srcPos
            diffEntry.put("新コードの位置", delta.getTarget().getPosition());      // tgtPos

            diffs.add(diffEntry);
        }

        return diffs;
    }

    private String localizeDiffType(String type) {
        switch (type.toUpperCase()) {
            case "CHANGE": return "変更";
            case "INSERT": return "追加";
            case "DELETE": return "削除";
            default: return "その他";
        }
    }
}
