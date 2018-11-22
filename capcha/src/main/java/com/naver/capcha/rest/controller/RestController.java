package com.naver.capcha.rest.controller;

import javax.annotation.Resource;
import java.math.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

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
	
	/*long startTime = System.currentTimeMillis(); 
	long endTime = System.currentTimeMillis(); 
	long diff = endTime - startTime; */
	
	long startTime; 
	long endTime; 
	 
	
	@RequestMapping(value="/getCategory", method=RequestMethod.GET)
	public List<FileCategoryVO> getImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FileCategoryVO> fvo = rs.getCategory();
		
		// Test Code (Must Delete)
		startTime = System.currentTimeMillis();
		
		return fvo;
	}
	
	// Func : Issue the client key after checking client_Id and client_Secret
	
	@RequestMapping(value="/nkey", method=RequestMethod.GET)
	public Map<String, String> nkey(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		int code = Integer.parseInt(request.getParameter("code"));
		String clientID = request.getHeader("X-Naver-Client-Id");
		String clientSecret = request.getHeader("X-Naver-Client-Secret");
		
		switch(code) {
			case 0:
				this.IssueKey();
				break;
			case 1:
				break;
			default:
				break;
		}
		
		Map<String, String> res = new HashMap<String, String>();
		res.put("clientID", clientID);
		res.put("clientSecret", clientSecret);
		res.put("code", code+"");
		
		res.put("IsTime", this.checkTime()+"");
        
		return res;
	}
	
	/*
	 * IssueKey()
	 * Issue the client key
	 */
	public void IssueKey() throws Exception{
		
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
	
	/*
	 * boolean checkTime()
	 * : Calculate the response time after issued key
	 */
	public boolean checkTime() throws Exception {
		endTime = System.currentTimeMillis();
        double diff = (endTime - startTime)/1000.0;
		if(0 < diff && diff <= 10) {
			return true;
		}
		return false;
	}
	
	/*
	 * String checkTime()
	 * : Calculate the response time after issued key
	 */
	public String checkClientValid(String id, String secret) throws Exception{
		
		return "";
	}
}
