package com.docmall.util;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docmall.dto.EmailDTO;
import com.docmall.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController // 컨트롤러 클래스가 ajax용도로만 사용될 경우 적용
@RequestMapping("/email/*") //현재는 jsp사용하지 않을 예정
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;
	
	//메일 인증코드 요청 주소
	@GetMapping("/authcode")
	public ResponseEntity<String> authSend(EmailDTO dto, HttpSession session){
		
		log.info("이메일 정보 : " + dto);
		
		ResponseEntity<String> entity = null;
		
		//인증코드 6자리 랜덤 생성
		String authCode = "";
		for(int i=0; i<6; i++) {
			authCode += String.valueOf((int)(Math.random()*10));
		}
		
		log.info("인증코드 : " + authCode);
		
		//사용자에게 메일로 발급해준 인증코드를 입력 시 비교목적으로 세션형태로 미리 저장해둔다.
		session.setAttribute("authCode", authCode);
		
		try {
			emailService.sendMail(dto, authCode); //메일 보내기
			entity = new ResponseEntity<String>("success",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	
	//인증번호 확인 = 세션형태로 저장한 정보를 이용 //L42:session.setAttribute("authCode", authCode);
	//세션 작업을 하기 위해선 세션이 호출되는 메서드에 파라미터로 HttpSession 삽입 필수
	@GetMapping("/confirmAuthcode")
	public ResponseEntity<String> confirmAuthcode(String authCode, HttpSession session){
		
		ResponseEntity<String> entity = null;
				
		if(session.getAttribute("authCode") != null) {
			
			//인증일치 여부
			if(authCode.equals(session.getAttribute("authCode"))) {
				entity = new ResponseEntity<String>("success",HttpStatus.OK);
			}else {
				entity = new ResponseEntity<String>("fail",HttpStatus.OK);
			}
			
		}else {
			//세션이 소멸된 경우
			entity = new ResponseEntity<String>("request",HttpStatus.OK);
		}		
		
		return entity;
	}
	
}
