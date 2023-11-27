package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;

public interface ReviewMapper {

	void review_insert(ReviewVO vo);
	
	void review_modify(ReviewVO vo);
	
	List<ReviewVO> list(@Param("pro_num") Integer pro_num, @Param("cri") Criteria cri); //검색필드 사용안함
	
	int listCount(Integer pro_num);
	
	void delete(Long rew_num);
}
