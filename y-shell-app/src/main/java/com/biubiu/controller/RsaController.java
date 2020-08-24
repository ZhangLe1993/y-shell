package com.biubiu.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "rsa")
public class RsaController {

    @RequestMapping("upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return new ResponseEntity<>(ImmutableMap.of("code","500","msg", "file is empty"), HttpStatus.OK);
        }
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String filePath = "/data/xxx";
        String path = filePath + fileName;
        File dest = new File(path);
        if(!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        multipartFile.transferTo(dest);
        return new ResponseEntity<>(ImmutableMap.of("code","200","msg", "success"), HttpStatus.OK);
    }
}
