package com.docmall.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.mapper.AdProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdProductServiceImpl implements AdProductService {

	private final AdProductMapper adProductMapper;

	@Override
	public void pro_insert(ProductVO vo) {

		adProductMapper.pro_insert(vo);
		
	}

	@Override
	public List<ProductVO> pro_list(Criteria cri) {
		
		return adProductMapper.pro_list(cri);
	}

	@Override
	public int getTotalCount(Criteria cri) {
		return adProductMapper.getTotalCount(cri);
	}
}
