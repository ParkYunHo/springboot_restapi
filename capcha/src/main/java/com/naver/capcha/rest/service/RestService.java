package com.naver.capcha.rest.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import com.naver.capcha.rest.domain.*;
import com.naver.capcha.rest.mapper.RestMapper;

import java.util.*;

@Service("com.naver.capcha.rest.service.RestService")
public class RestService {
	@Resource(name="com.naver.capcha.rest.mapper.RestMapper")
	RestMapper rm;
	
	public List<FileCategoryVO> getCategory() throws Exception{
		return rm.getCategory();
	}
	
	public int setFile(FileVO vo) throws Exception{
		return rm.setFile(vo);
	}
	
	public FileVO getFile(int fno) throws Exception{
		return rm.getFile(fno);
	}
	
	public int setRegister(RegisterVO vo) throws Exception{
		vo.setClientID(RandomStringUtils.randomAlphanumeric(20));
		vo.setClientSecret(RandomStringUtils.randomAscii(10));
		vo.setClientKey(RandomStringUtils.randomAlphanumeric(16));
		
		return rm.setClient(vo);
	}
	
	public String checkClientValid(String id, String secret) throws Exception{
		RegisterVO vo = new RegisterVO();
		vo.setClientID(id);
		vo.setClientSecret(secret);
		
		
		return "";
	}
}
