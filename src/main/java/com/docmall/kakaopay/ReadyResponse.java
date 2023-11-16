package com.docmall.kakaopay;

import lombok.Data;

//결제요청(1차) 의 응답결과를 저장하기 위한 클래스.

@Data
public class ReadyResponse {

	private String tid; //	결제 고유 번호, 20자
	private String next_redirect_pc_url; // 요청한 클라이언트가 PC 웹일 경우
	private String partner_odrder_id; // 가맹점 주문번호	
	private String created_at; //결제 준비 요청 시간
}
