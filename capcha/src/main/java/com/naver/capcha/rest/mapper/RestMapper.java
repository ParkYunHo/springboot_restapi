package com.naver.capcha.rest.mapper;

import org.springframework.stereotype.Repository;
import com.naver.capcha.rest.domain.*;

import com.naver.capcha.rest.domain.FileCategoryVO;
import java.util.*;

@Repository("com.naver.capcha.rest.mapper.RestMapper")
public interface RestMapper {
	public List<FileCategoryVO> getCategory() throws Exception;
	public int setFile(FileVO vo) throws Exception;
	public FileVO getFile(int fno) throws Exception;
}
