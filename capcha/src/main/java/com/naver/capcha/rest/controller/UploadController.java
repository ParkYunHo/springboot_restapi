package com.naver.capcha.rest.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.naver.capcha.rest.domain.*;
import com.naver.capcha.rest.service.*;

@Controller
public class UploadController {
	@Resource(name="com.naver.capcha.rest.service.RestService")
	RestService rs;
	
	@RequestMapping(value="/imgUpload")
	public String imgUpload(Model model) throws Exception {
		model.addAttribute("categoryList", rs.getCategory());
		
		return "/imgUpload";
	}
	
	@RequestMapping(value="/imgUploadProc")
	public ResponseEntity<?> imgUploadProc(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception{
		String fileName = file.getOriginalFilename(); 
        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase(); 
        // uploadFiles folder 오른쪽 클릭 Properties > Location (끝에 \\ 붙일것!)
        // String fileUrl = "C:\\dev\\sts-3.9.6.RELEASE\\capcha\\src\\main\\webapp\\WEB-INF\\views\\imgUpload\\";
        String fileUrl = "C:\\Users\\박윤호.DESKTOP-FUNI6TP\\git\\springboot_restapi\\capcha\\src\\main\\webapp\\WEB-INF\\views\\imgUpload\\";
        
        File dsFile; 
        String dsFileName;
	        
        do { 
        	dsFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileExtension; 
            dsFile = new File(fileUrl + dsFileName); 
        } while (dsFile.exists()); 
        
        // Img파일을 Server 내에 저장하는 부
        dsFile.getParentFile().mkdirs(); 
        file.transferTo(dsFile);
        
        // file정보를 VO class에 담아서 DB에 저장하는 부분  
        FileVO vo = new FileVO();
        vo.setCno(Integer.parseInt(request.getParameter("category")));
        vo.setName(dsFileName);
        vo.setOriname(fileName);
        vo.setUrl(fileUrl);
        rs.setFile(vo);
		
        return new ResponseEntity("Successfully uploaded - " + fileName, HttpStatus.OK);
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register() throws Exception{
		return "/register";
	}
	
	@RequestMapping(value="/registerProc")
	public ResponseEntity<?> registerProc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		RegisterVO vo = new RegisterVO();
		
		vo.setAppName(request.getParameter("appName").toString());
		try {
			rs.setRegister(vo);
		}catch(Exception e) {
			return new ResponseEntity<>("Register Fail", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Register Success", HttpStatus.OK);
	}
}
