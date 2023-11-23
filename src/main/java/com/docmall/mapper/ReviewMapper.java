package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;

public interface ReviewMapper {

	void review_insert(ReviewVO vo);
	
	List<ReviewVO> list(@Param("pro_num") Integer pro_num, @Param("cri") Criteria cri); //Criteria 검색 기능은 사용하지 않음
	
	int listCount(Integer pro_num);
}
