package com.docmall.service;

import java.util.List;

import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;

public interface UserProductService {

	//2차카테고리별 상품 리스트
	List<ProductVO> pro_list(Integer cg_code,Criteria cri);		
	
	int getTotalCount(Integer cg_code);
}
