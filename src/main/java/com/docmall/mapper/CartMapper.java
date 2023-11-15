package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CartVO;
import com.docmall.dto.CartListDTO;

public interface CartMapper {

	void cart_add(CartVO vo);
	
	List<CartListDTO> cart_list(String mbsp_id);
	
	void cart_amount_change(@Param("cart_code") Long cart_code, @Param("cart_amount") int cart_amount);
	
	void cart_list_del(Long cart_code);
}
