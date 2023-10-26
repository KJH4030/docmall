package com.docmall.service;

import org.springframework.stereotype.Service;

import com.docmall.domain.MemberVO;
import com.docmall.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	//자동주입
	//@RequiredArgsConstructor : memberMapper 필드를 매개변수로 하는 필드 생성
	private final MemberMapper memberMapper;

	/*
	private MemberServiceImpl(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	*/

	//아이디 중복 체크
	@Override
	public String idCheck(String mbsp_id) {
		return memberMapper.idCheck(mbsp_id);
	}

	@Override
	public void join(MemberVO vo) {
		
		memberMapper.join(vo);
		
	}

	@Override
	public MemberVO login(String mbsp_id) {
		
		return memberMapper.login(mbsp_id);
	}
	
}
