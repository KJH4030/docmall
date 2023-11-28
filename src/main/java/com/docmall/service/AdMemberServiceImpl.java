package com.docmall.service;

import org.springframework.stereotype.Service;

import com.docmall.mapper.AdMemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdMemberServiceImpl implements AdMemberService {

	private final AdMemberMapper adMemberMapper;
	
}
