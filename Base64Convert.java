/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.casemanage.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wWX1043508
 * @since 2021/04/23
 */
public class Base64Convert {
    public FileJsonParam fileJsonParam = new FileJsonParam();

    /**
     * @param args args
     */
    public static void main(String[] args) {
        String fileName = "项目表设计.xlsx";
        String targetFileName = "项目表设计.xlsx";
        toCodeFile(accuqreFileJsonParam(fileName, fileName));
    }

    private static void outToFile(String jsonPath) {
        Base64Convert baseCov = new Base64Convert();
        String jsonStr = baseCov.readFromFile(jsonPath);
        baseCov.fileJsonParam = JSON.parseObject(jsonStr, FileJsonParam.class);
        String targetFullPath = baseCov.fileJsonParam.targetFilePath;
        StringBuilder text = new StringBuilder();
        baseCov.fileJsonParam.targetFileList.forEach(filename -> {
            baseCov.readFromFile(baseCov.fileJsonParam.targetFilePath + filename, text);
        });
        try {
            baseCov.base64ToIo(text.toString(),
                baseCov.fileJsonParam.targetFilePath + "json" + baseCov.fileJsonParam.targetFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileJsonParam accuqreFileJsonParam(String fileName, String targetFileName) {
        FileJsonParam fileJsonParam = new FileJsonParam();
        fileJsonParam.filePath = "D:\\文档\\test\\";
        fileJsonParam.targetFilePath = "D:\\文档\\test\\";
        fileJsonParam.fileName = fileName;
        fileJsonParam.targetFileName = targetFileName;
        // fileJsonParam.jsonPath = "D:\\文档\\test\\华为考试通过截图.rar.json";
        return fileJsonParam;

    }

    private static void toCodeFile(FileJsonParam fileJsonParam) {
        try {
            Base64Convert baseCov = new Base64Convert();
            baseCov.fileJsonParam = fileJsonParam;
            baseCov.fileJsonParam.fullPath = baseCov.fileJsonParam.filePath + baseCov.fileJsonParam.fileName;
            baseCov.fileJsonParam.targetFullPath = baseCov.fileJsonParam.targetFilePath
                + baseCov.fileJsonParam.targetFileName;
            baseCov.fileJsonParam.jsonPath = baseCov.fileJsonParam.targetFullPath + ".json";
            String strBase64 = baseCov.ioToBase64(baseCov.fileJsonParam.fullPath); //将 io 转换为 base64编码
            baseCov.fileJsonParam.fileSize = strBase64.length();
            int filenameCount = 0;
            int lastSize = 0;
            while (true) {
                filenameCount++;
                int nextsize = lastSize + baseCov.fileJsonParam.splitSize;
                if (nextsize > strBase64.length()) {
                    nextsize = strBase64.length();
                }
                baseCov.fileJsonParam.targetFileList.add(baseCov.fileJsonParam.targetFileName + filenameCount + ".txt");
                baseCov.writeToFile(strBase64.substring(lastSize, nextsize),
                    baseCov.fileJsonParam.targetFullPath + filenameCount + ".txt");
                lastSize = nextsize;
                if (nextsize >= strBase64.length()) {
                    baseCov.writeToFile(JSON.toJSONString(baseCov.fileJsonParam, SerializerFeature.PrettyFormat,
                        SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat),
                        baseCov.fileJsonParam.targetFullPath + ".json");
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName fileName
     * @return String
     * @throws IOException IOException
     */
    public String ioToBase64(String fileName) throws IOException {
        String strBase64 = null;
        try {
            InputStream in = new FileInputStream(fileName);
            // in.available()返回文件的字节长度
            byte[] bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
            in.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return strBase64;
    }

    // 将 base64编码转换为 io 文件流，生成一幅新图片

    /**
     * @param strBase64 strBase64
     * @param fileName fileName
     * @throws IOException IOException
     */
    public void base64ToIo(String strBase64, String fileName) throws IOException {
        String string = strBase64;
        byte[] bytes = new BASE64Decoder().decodeBuffer(string);
        byte[] buffer = new byte[1024];
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            FileOutputStream out = new FileOutputStream(fileName)) {
            // 解码，然后将字节转换为文件
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); //文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @param fileName fileName
     * @return boolean
     */
    public static boolean isExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * @param path path
     * @return String
     */
    public String readFromFile(String path) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String str;
            while ((str = in.readLine()) != null) {
                text.append(str);
                text.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    /**
     * @param path path
     * @return String
     */
    public StringBuilder readFromFile(String path, StringBuilder text) {
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String str;
            while ((str = in.readLine()) != null) {
                text.append(str);
                text.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    //将string写入文件中

    /**
     * @param str str
     * @param path path
     */
    public void writeToFile(String str, String path) {
        // 新建文件夹
        File file = new File(path);
        if (!isExist(path)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
