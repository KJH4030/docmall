package com.docmall.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.domain.CategoryVO;
import com.docmall.mapper.UserCategoryMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // bean이 생성 및 등록될 설정작업 : userCategoryServiceImpl bean 생성
public class UserCategoryServiceImpl implements UserCategoryService {

	private final UserCategoryMapper userCategoryMapper;
	

	@Override
	public List<CategoryVO> getSecondCategoryList(Integer cg_parent_code) {

		return userCategoryMapper.getSecondCategoryList(cg_parent_code);
	}
}
