package com.biubiu.service;

import com.biubiu.core.common.Const;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class RsaService {

    private final static Logger logger = LoggerFactory.getLogger(RsaService.class);

    public ResponseEntity<?> upload(MultipartFile multipartFile, String host) throws IOException {
        if(multipartFile.isEmpty()) {
            return new ResponseEntity<>(ImmutableMap.of("code","500","msg", "file is empty"), HttpStatus.OK);
        }
        String fileName = multipartFile.getOriginalFilename();
        logger.info("上传的文件名: {}", fileName);
        assert fileName != null;
        // String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // logger.info("前椎名: {}", suffixName);
        String path = Const.upload + host + "/";
        // String path = filePath + fileName;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        multipartFile.transferTo(new File(folder, fileName));
        return new ResponseEntity<>(ImmutableMap.of("code","200","msg", "success"), HttpStatus.OK);
    }
}
