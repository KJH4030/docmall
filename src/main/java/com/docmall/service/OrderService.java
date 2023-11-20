package com.docmall.service;

import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;

public interface OrderService {

	int getOderSeq();
	
	//주문하기
	void order_insert(OrderVO o_vo, PaymentVO p_vo);
}
