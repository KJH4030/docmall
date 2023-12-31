package com.docmall.domain;

import java.util.Date;

import lombok.Data;

//pro_num, cg_code, pro_name, pro_price, pro_discount, pro_publisher, pro_content, 
//pro_up_folder, pro_img, pro_amount, pro_buy, pro_date, pro_updatedate
@Data
public class ProductVO {

	private Integer pro_num; //시퀀스 생성
	
	private Integer cg_code; //2차카테고리 코드
	private String pro_name;
	private int pro_price;
	private int pro_discount;
	private String pro_publisher;
	private String pro_content;
	
	private String pro_up_folder; //별도 스프링 처리
	private String pro_img; //별도 스프링 처리
	
	private int pro_amount;
	private String pro_buy;
	
	private Date pro_date;
	private Date pro_updatedate;
	
	//private MultipartFile uploadFile;
}
