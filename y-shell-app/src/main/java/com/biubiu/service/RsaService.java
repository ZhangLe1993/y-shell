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
import java.util.HashSet;
import java.util.Set;

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
        // 判断文件是否存在
        Set<String> names = listFileName(host);
        String finalName = fileName;
        if(names.contains(fileName)) {
            long count = names.stream().filter(p -> p.contains(fileName)).count();
            finalName = fileName + (count - 1);
        }
        multipartFile.transferTo(new File(folder, finalName));
        return new ResponseEntity<>(ImmutableMap.of("code","200","msg", "success"), HttpStatus.OK);
    }

    public Set<String> listFileName(String host) {
        Set<String> inFiles = new HashSet<>();
        String path = Const.upload + host + "/";
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return new HashSet<>();
            } else {
                for (File file2 : files) {
                    inFiles.add(file2.getAbsolutePath().replace(path, ""));
                }
            }
        }
        return inFiles;
    }

    public static void main(String[] args) {
        Set<String> inFiles = new HashSet<>();
        String path = Const.upload + "122222" + "/";
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return ;
            } else {
                for (File file2 : files) {
                    inFiles.add(file2.getAbsolutePath().replace(path, ""));
                }
            }
        } else {
            logger.info("目录不存在！");
        }
    }
}
