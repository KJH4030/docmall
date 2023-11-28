package com.docmall.mapper;

import java.util.List;

import com.docmall.domain.OrderVO;
import com.docmall.dto.Criteria;

public interface AdOrderMapper {

	
	List<OrderVO> order_list(Criteria cri);
	
	int getTotalCount(Criteria cri);
	
}
