package com.biubiu.dto;

import com.biubiu.model.Remote;
import lombok.Data;

import java.util.List;

@Data
public class Node {
    private Long id;
    private String name;
    private String icon;
    private String type;
    private Remote config;
    private List<Node> children;
}
