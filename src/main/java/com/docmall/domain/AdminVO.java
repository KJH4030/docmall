package com.docmall.domain;

import java.util.Date;

import lombok.Data;

@Data
public class AdminVO {
	
	private String admin_id;
	private String admin_pw;
	private Date admin_visit_date;
		
}
