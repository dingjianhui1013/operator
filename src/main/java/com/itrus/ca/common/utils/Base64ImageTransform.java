package com.itrus.ca.common.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

public class Base64ImageTransform{
	
    public static void main(String[] args){  
        String strImg = GetImageStr("d://TEST.jpg");  
        System.out.println(strImg);  
        GenerateImage(strImg,"d://TEST1.jpg");  
    }  

    /**
     * 图片转化成base64字符串  
     * @param imgFile  图片路径
     * @return
     */
    public static String GetImageStr(String imgFile){//将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        //String imgFile = "d://test.jpg";//待处理的图片  
        InputStream in = null;  
        byte[] data = null;  
        //读取图片字节数组  
        try{  
            in = new FileInputStream(imgFile);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e){  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        Base64 encoder = new Base64();  
        return new String(encoder.encode(data));//返回Base64编码过的字节数组字符串  
    }  
      
    /**
     * base64字符串转化成图片 
     * @param imgStr  图片base64 字符串
     * @param path 图片保存路径+图片名称
     * @return
     */
    public static boolean GenerateImage(String imgStr,String path){   //对字节数组字符串进行Base64解码并生成图片  
        if (imgStr == null) //图像数据为空  
            return false;  
        Base64 decoder = new Base64();  
        try{  
            //Base64解码  
            byte[] b = decoder.decodeBase64(imgStr);  
            for(int i=0;i<b.length;++i){  
                if(b[i]<0){//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            //生成jpeg图片  
            //String imgFilePath = "d://222.jpg";//新生成的图片  
            OutputStream out = new FileOutputStream(path);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }catch (Exception e){  
            return false;  
        }  
    }   
}
