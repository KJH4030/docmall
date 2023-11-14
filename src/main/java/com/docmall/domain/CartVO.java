package com.docmall.domain;

import lombok.Data;

@Data
public class CartVO {

	private Long cart_code;
	
	private Integer pro_num;
	private String mbsp_id;
	
	private int cart_amount;	
}
