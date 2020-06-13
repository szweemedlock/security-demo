package com.xavier.spring.securitydemo.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RestController
@RequestMapping("file/api")
public class FileConvertController {

    @GetMapping(value = "view", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> viewPicture() throws FileNotFoundException {
        File src = new File("D:\\99_projectfordixin\\test.jpg");
        InputStream inputStream = new FileInputStream(src);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
    }
}
