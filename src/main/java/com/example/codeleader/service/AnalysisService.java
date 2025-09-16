package com.example.codeleader.service;

import com.example.codeleader.model.MethodDiff;
import com.example.codeleader.model.MethodInfo;
import com.example.codeleader.util.JavaParserUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalysisService {

    public List<MethodInfo> parse(String source) {
        return JavaParserUtil.parseMethods(source);
    }

    public List<MethodDiff> diff(String oldSource, String newSource) {
        List<MethodInfo> oldMethods = JavaParserUtil.parseMethods(oldSource == null ? "" : oldSource);
        List<MethodInfo> newMethods = JavaParserUtil.parseMethods(newSource == null ? "" : newSource);

        Map<String, MethodInfo> oldMap = new HashMap<>();
        for (MethodInfo m : oldMethods) oldMap.put(JavaParserUtil.keyOf(m), m);

        Map<String, MethodInfo> newMap = new HashMap<>();
        for (MethodInfo m : newMethods) newMap.put(JavaParserUtil.keyOf(m), m);

        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(oldMap.keySet());
        keys.addAll(newMap.keySet());

        List<MethodDiff> diffs = new ArrayList<>();
        for (String k : keys) {
            MethodInfo oldM = oldMap.get(k);
            MethodInfo newM = newMap.get(k);
            MethodDiff md = new MethodDiff();
            if (newM != null) {
                md.setClassName(newM.getClassName());
                md.setMethodName(newM.getMethodName());
                md.setSignature(newM.getSignature());
                md.setBeginLine(newM.getBeginLine());
                md.setEndLine(newM.getEndLine());
            } else if (oldM != null) {
                md.setClassName(oldM.getClassName());
                md.setMethodName(oldM.getMethodName());
                md.setSignature(oldM.getSignature());
                md.setBeginLine(oldM.getBeginLine());
                md.setEndLine(oldM.getEndLine());
            }

            if (oldM == null && newM != null) {
                md.setStatus("added");
                md.setOldSource(null);
                md.setNewSource(newM.getSource());
            } else if (oldM != null && newM == null) {
                md.setStatus("removed");
                md.setOldSource(oldM.getSource());
                md.setNewSource(null);
            } else {
                // both exist -> compare source text
                if (Objects.equals(normalize(oldM.getSource()), normalize(newM.getSource()))) {
                    md.setStatus("unchanged");
                    md.setOldSource(oldM.getSource());
                    md.setNewSource(newM.getSource());
                } else {
                    md.setStatus("modified");
                    md.setOldSource(oldM.getSource());
                    md.setNewSource(newM.getSource());
                }
            }
            diffs.add(md);
        }
        return diffs;
    }

    private String normalize(String s) {
        if (s == null) return null;
        return s.replaceAll("\\s+", " ").trim();
    }
}
