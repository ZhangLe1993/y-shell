package com.biubiu.controller;

import com.biubiu.core.annotation.SystemLog;
import com.biubiu.service.RsaService;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(value = "rsa")
public class RsaController {

    private final static Logger logger = LoggerFactory.getLogger(RsaController.class);

    @Autowired
    private RsaService rsaService;

    @SystemLog(description = "上传密钥文件")
    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("host") String host) throws IOException {
        try{
            return rsaService.upload(multipartFile, host);
        }catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(ImmutableMap.of(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SystemLog(description = "获取对应主机的密钥文件")
    @GetMapping("list")
    public ResponseEntity<?> getFileList(@RequestParam("host") String host) throws IOException {
        try{
            Set<String> set = rsaService.listFileName(host);
            return new ResponseEntity<>(set, HttpStatus.OK);
        }catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(new HashSet<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
