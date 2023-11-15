package com.docmall.service;

import org.springframework.stereotype.Service;

import com.docmall.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

	private final OrderMapper orderMapper;
}
