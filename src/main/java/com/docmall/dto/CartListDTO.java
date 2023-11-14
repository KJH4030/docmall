package com.docmall.dto;

import lombok.Data;

@Data
public class CartListDTO {


	 
	private Integer pro_num;
	private Long cart_code; 
	
	private int pro_price;
	private int pro_discount;
	private int cart_amount;
	
	private String pro_name;
	
	private String pro_img;	
	private String pro_up_folder;
}
