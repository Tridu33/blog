/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.casemanage.test;

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
 * @date 2021/4/21 16:59
 */
public class Base64Convert {
    public static void main(String[] args) {
        try {
            Base64Convert baseCov = new Base64Convert();
            String strBase64 = baseCov.ioToBase64("D:\\文档\\test\\tezxt.png"); //将 io 转换为 base64编码
            System.out.println(">>> " + strBase64);
            baseCov.writeToFile(strBase64, "D:\\文档\\test\\tezxt.txt");
            strBase64 = baseCov.readFromFile("D:\\文档\\test\\tezxt.txt");
            System.out.println("2>>> " + strBase64);
            // baseCov.base64ToIo(strBase64, "D:\\文档\\test\\tezxt1.png"); //将 base64编码转换为 io 文件流，生成一幅新图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BASE64Decoder decoder = new BASE64Decoder();

    public String ioToBase64(String fileName) throws IOException {
        // String fileName = "d:/gril.gif"; //源文件
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

    public void base64ToIo(String strBase64, String fileName) throws IOException {
        String string = strBase64;
        // String fileName = "d:/gril2.gif"; //生成的新文件
        try {
            // 解码，然后将字节转换为文件
            byte[] bytes = new BASE64Decoder().decodeBuffer(string);   //将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            FileOutputStream out = new FileOutputStream(fileName);
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

    //判断文件是否存在
    public static boolean isExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public String readFromFile(String path) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine()) != null) {
                // System.out.println(str);
                text.append(str);
            }
            // System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    //将string写入文件中
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
