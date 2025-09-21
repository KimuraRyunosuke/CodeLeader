package com.example.codeleader.service;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiffService {

    // type を日本語化
    private String localizeDiffType(String type) {
        switch (type.toUpperCase()) {
            case "CHANGE": return "変更";
            case "INSERT": return "追加";
            case "DELETE": return "削除";
            default: return "その他";
        }
    }

    public List<Map<String, Object>> getDiff(String oldSource, String newSource) {
        List<String> original = Arrays.asList(oldSource.split("\n"));
        List<String> revised = Arrays.asList(newSource.split("\n"));

        Patch<String> patch = DiffUtils.diff(original, revised);
        List<Map<String, Object>> diffs = new ArrayList<>();

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            List<String> sourceLines = delta.getSource().getLines();
            List<String> targetLines = delta.getTarget().getLines();

            // 空行だけの差分は無視
            boolean sourceOnlyBlank = sourceLines.stream().allMatch(String::isBlank);
            boolean targetOnlyBlank = targetLines.stream().allMatch(String::isBlank);
            if (sourceOnlyBlank && targetOnlyBlank) continue;

            Map<String, Object> diffEntry = new HashMap<>();
            diffEntry.put("変更種別", localizeDiffType(delta.getType().toString()));
            diffEntry.put("旧コードの位置", delta.getSource().getPosition());
            diffEntry.put("新コードの位置", delta.getTarget().getPosition());
            diffEntry.put("旧コードの内容", sourceLines);
            diffEntry.put("新コードの内容", targetLines);

            diffs.add(diffEntry);
        }

        return diffs;
    }
}
