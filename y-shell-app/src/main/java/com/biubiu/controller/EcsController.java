package com.biubiu.controller;

import com.biubiu.core.annotation.SystemLog;
import com.biubiu.dto.Node;
import com.biubiu.pojo.Ecs;
import com.biubiu.service.EcsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "ecs")
public class EcsController {


    private final static Logger logger = LoggerFactory.getLogger(EcsController.class);

    @Autowired
    private EcsService ecsService;

    @SystemLog(description = "获取远程连接树")
    @GetMapping(value = "tree")
    public ResponseEntity<?> getSshTree() {
        try {
            List<Node> tree = ecsService.getTree();
            return new ResponseEntity<>(tree, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SystemLog(description = "创建会话")
    @PostMapping(value = "")
    public ResponseEntity<?> insert(@RequestBody Ecs ecs) {
        try {
            int count = ecsService.insert(ecs);
            if(count > 0) {
                return new ResponseEntity<>(ecs.getId(), HttpStatus.OK);
            }
            return new ResponseEntity<>(0, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SystemLog(description = "更新会话")
    @PutMapping(value = "")
    public ResponseEntity<?> update(@RequestBody Ecs ecs) {
        try {
            int count = ecsService.update(ecs);
            if(count > 0) {
                return new ResponseEntity<>(ecs.getId(), HttpStatus.OK);
            }
            return new ResponseEntity<>(0, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SystemLog(description = "删除会话")
    @DeleteMapping(value = "")
    public ResponseEntity<?> delete(Long id) {
        try {
            int count = ecsService.delete(id);
            if(count > 0) {
                return new ResponseEntity<>(id, HttpStatus.OK);
            }
            return new ResponseEntity<>(0, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
