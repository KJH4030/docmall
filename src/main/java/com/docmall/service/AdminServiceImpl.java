package com.docmall.service;

import org.springframework.stereotype.Service;

import com.docmall.domain.AdminVO;
import com.docmall.mapper.AdminMapper;

import lombok.RequiredArgsConstructor;

@Service // bean 생성 및 등록 : adminServiceImpl
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AdminMapper adminMapper;

	@Override
	public AdminVO admin_ok(String admin_id) {
		
		return adminMapper.admin_ok(admin_id);
	}
}
