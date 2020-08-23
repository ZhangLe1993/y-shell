package com.biubiu.dto;

import lombok.Data;

import java.util.List;

@Data
public class Node {
    private Long id;
    private String name;
    private String icon;
    private String type;
    private List<Node> children;
}
