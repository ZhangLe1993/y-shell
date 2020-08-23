package com.biubiu.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Ecs {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String config;
    private Long parentId;
    private String createTime;
    private String updateTime;
}
