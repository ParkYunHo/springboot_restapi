package com.naver.capcha.rest.mapper;

import org.springframework.stereotype.Repository;
import com.naver.capcha.rest.domain.*;

import java.util.*;

@Repository("com.naver.capcha.rest.mapper.RestMapper")
public interface RestMapper {
	public List<FileCategoryVO> getCategory() throws Exception;
	public int setFile(FileVO vo) throws Exception;
	public FileVO getFile(int fno) throws Exception;
	public int setClient(RegisterVO vo) throws Exception;
	public int checkClientValid(RegisterVO vo) throws Exception;
	public int setKey(RegisterVO vo) throws Exception;
	public int checkKeyValid(RegisterVO vo) throws Exception;
	public int setIsIssued(RegisterVO vo) throws Exception;
	public int randomImage(RegisterVO vo) throws Exception;
}
