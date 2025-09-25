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
            diffEntry.put("type", delta.getType().toString());
            diffEntry.put("source", delta.getSource().getLines());
            diffEntry.put("target", delta.getTarget().getLines());
            diffEntry.put("srcPos", delta.getSource().getPosition());
            diffEntry.put("tgtPos", delta.getTarget().getPosition());

            diffs.add(diffEntry);
        }

        return diffs;
    }
}
