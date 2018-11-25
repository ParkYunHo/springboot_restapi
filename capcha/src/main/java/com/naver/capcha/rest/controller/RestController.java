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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Resource(name="com.naver.capcha.rest.service.RestService")
	RestService rs;
	
	long startTime; 
	long endTime; 
	
	// Func : Issue the client key after checking client_Id and client_Secret
	@RequestMapping(value="/nkey", method=RequestMethod.GET)
	public Map<String, String> nkey(@RequestParam(value="code", required = true, defaultValue = "0") String code, @RequestParam(value="value", required = true, defaultValue = "") String value, HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String clientID = request.getHeader("X-Naver-Client-Id");
		String clientSecret = request.getHeader("X-Naver-Client-Secret");
		
		Map<String, String> res = new HashMap<String, String>();
		
		if(this.checkClientValid(clientID, clientSecret) == 1) {
			switch(code) {
				case "0":
					res.put("key", this.IssueKey(clientID, clientSecret));
					response.setStatus(200);
					startTime = System.currentTimeMillis(); // Timer(0~7200) 기능을 위해 StartTime을 저장
					break;
				case "1":
					break;
				default:
					res.put("error", "code value does not valid!");
					break;
			}
//			res.put("IsTime", this.checkTime()+"");
		}else {
			res.put("error", "ClientId and ClientSecret does not valid!");
		}
        
		return res;
	}
	
	// func : Call the Image api 
	@RequestMapping(value="/ncaptcha.bin")
	public Map<String, String> capchaImg(@RequestParam(value="key", required=true, defaultValue="") String key, HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String clientID = request.getHeader("X-Naver-Client-Id");
		String clientSecret = request.getHeader("X-Naver-Client-Secret");
		
		Map<String, String> res = new HashMap<String, String>();
		
		if(this.checkClientValid(clientID, clientSecret) == 1) {
			if(this.checkKeyValid(clientID, clientSecret, key) == 1) {
				this.randomImage(clientID, clientSecret);
				
				// Server 내부에 저장된 File 가져오는 부분 (추후에 API로 가져오도록 변경)
				FileVO fileVO = rs.getFile(6);
				String fileUrl = fileVO.getUrl();
	            String fileName = fileVO.getName();
	            /////////////////////////////////////////////////////
				
				//파일 업로드된 경로 
		        try{
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
		                rs.setIsIssued(clientID, clientSecret);
		            }
		        } catch (Exception e) {
		        	response.setContentType("application/json;charset=UTF-8");
		        	response.setStatus(400);
		        	res.put("error", "Unissued image(이미지 발급을 하지 않음)");
		        }
			}else {
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(403);
				res.put("error", "Invalid key. (키가 만료되거나 없는 키)");
			}
		}else {
			response.setContentType("application/json;charset=UTF-8");
			res.put("error", "ClientId and ClientSecret does not valid!");
		}
		return res;
	}
	
	// func : Issue the client key 
	public String IssueKey(String id, String secret) throws Exception{
		RegisterVO vo = new RegisterVO();
		vo.setClientID(id);
		vo.setClientSecret(secret);
		return rs.setKey(vo);
	}
	
	public void randomImage(String id, String secret) throws Exception{
		rs.randomImage(id, secret);
	}
	
	// func : Calculate the response time after issued key
	public boolean checkTime() throws Exception {
		endTime = System.currentTimeMillis();
        double diff = (endTime - startTime)/1000.0;
		if(0 < diff && diff <= 10) {
			return true;
		}
		return false;
	}
	
	// func : the client id and secret value validation check (1:valid, 0:not valid)
	public int checkClientValid(String id, String secret) throws Exception{
		return rs.checkClientValid(id, secret);
	}
	
	// func : the key value validation check (1:valid, 0:not valid)
	public int checkKeyValid(String id, String secret, String key) throws Exception{
		return rs.checkKeyValid(id, secret, key);
	}
}
