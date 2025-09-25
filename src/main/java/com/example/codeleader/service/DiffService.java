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
            diffEntry.put("種類", delta.getType().toString());
            diffEntry.put("元コード", delta.getSource().getLines());
            diffEntry.put("新コード", delta.getTarget().getLines());
            diffEntry.put("元の位置", delta.getSource().getPosition());
            diffEntry.put("変更後位置", delta.getTarget().getPosition());

            diffs.add(diffEntry);
        }

        return diffs;
    }
}
