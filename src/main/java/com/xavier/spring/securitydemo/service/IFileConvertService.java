package com.xavier.spring.securitydemo.service;

import org.jodconverter.office.OfficeException;

import java.io.File;

/**
 * 处理文件转换
 * @author Karl Xavier
 */
public interface IFileConvertService {

    /**
     * 讲文件转为pdf
     * @param src 原文件
     * @param target pdf文件
     * @return 转换结果
     */
    boolean toPdf(File src, File target) throws OfficeException;
}
