package com.docmall.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.service.AdMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequiredArgsConstructor
@RequestMapping("/admin/member/*")
@Log4j
@ControllerAdvice
public class AdMemberController {

	private final AdMemberService adMemberService;
}
