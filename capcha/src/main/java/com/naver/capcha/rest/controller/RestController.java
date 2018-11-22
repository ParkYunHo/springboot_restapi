package com.naver.capcha.rest.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import com.naver.capcha.rest.domain.*;
import com.naver.capcha.rest.service.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Resource(name="com.naver.capcha.rest.service.RestService")
	RestService rs;
	
	@RequestMapping(value="/getCategory", method=RequestMethod.GET)
	public List<FileCategoryVO> getImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FileCategoryVO> fvo = rs.getCategory();
		return fvo;
	}
	
	@RequestMapping(value="/getCapchaImg", method=RequestMethod.GET)
	public void getCapchaImg(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
        FileVO fileVO = rs.getFile(13);
        
        //파일 업로드된 경로 
        try{
            String fileUrl = fileVO.getUrl();
            String fileName = fileVO.getName();
            InputStream in = null;
            File file = null;
            boolean skip = false;
            
            //파일을 읽어 스트림에 담기  
            try{
                file = new File(fileUrl, fileName);
                in = new FileInputStream(file);
            } catch (FileNotFoundException fe) {
                skip = true;
            }
            
            if(!skip) {
            	response.setContentType("image/jpeg;charset=UTF-8");
            	
            	OutputStream out = response.getOutputStream();
                byte b[] = new byte[(int)file.length()];
                int leng = 0;
                while ((leng = in.read(b)) > 0) {
                	out.write(b, 0, leng);
                }
                in.close();
            }else {
            	response.setContentType("application/json;charset=UTF-8");
            	System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
            }
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
	}
	
	
}
