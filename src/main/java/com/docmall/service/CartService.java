package com.docmall.service;

import java.util.List;

import com.docmall.domain.CartVO;
import com.docmall.dto.CartListDTO;

public interface CartService {

	void cart_add(CartVO vo);

	List<CartListDTO> cart_list(String mbsp_id);
	
	void cart_amount_change(Long cart_code, int cart_amount);
	
	void cart_list_del(Long cart_code);
}
