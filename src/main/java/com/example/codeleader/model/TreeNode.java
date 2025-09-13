package com.example.codeleader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TreeNode {
    private String id;
    private String name;
    private String type;
    private boolean changed;
    private List<TreeNode> children;
}
