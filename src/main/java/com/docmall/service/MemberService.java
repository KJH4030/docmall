package com.docmall.service;

import com.docmall.domain.MemberVO;

public interface MemberService {

	//아이디 중복 체크
	String idCheck(String mbsp_id);

	public void join(MemberVO vo);
	
	MemberVO login(String mbsp_id);
}
