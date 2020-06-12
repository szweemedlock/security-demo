package com.xavier.spring.securitydemo.service.impl;

import com.xavier.spring.securitydemo.service.IFileConvertService;
import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class FileConvertServiceImpl implements IFileConvertService {

    @Resource
    private DocumentConverter documentConverter;

    @Override
    public boolean toPdf(File src, File target) throws OfficeException {
        documentConverter.convert(src).to(target).execute();
        return true;
    }
}
