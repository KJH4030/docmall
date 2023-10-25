package com.docmall.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/member/*")
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	// https://dev-coco.tistory.com/70
	
	private final MemberService memberService;
	
	@GetMapping("/join")
	public void join() {
		
		log.info("called... join");
	}
	
	//비동기방식. ajax문법으로 호출
	//아이디 중복 체크
	// responseEntity ? httpentity를 상속받는, 결과데이터와 HTTP 상태코드를 직접 제어 가능
	//3가지 구성 요소 - HttpStatus, HttpHeaders, HTtpBody
	// ajax기능과 함꼐 사용
	@GetMapping("/idCheck")
	public ResponseEntity<String> idCheck(String mbsp_id){
		
		log.info("아이디 : " + mbsp_id);
		
		ResponseEntity<String> entity = null;
		
		//서비스 메서드 호출 구문 작업.
		String idUse = "";
		if(memberService.idCheck(mbsp_id) != null) {
			idUse= "no"; //아이디가 존재하여 사용 불가
		}else {
			idUse="yes";
		}
		
		entity = new ResponseEntity<String>(idUse, HttpStatus.OK);
		
		return entity;
		
	}
	
}
