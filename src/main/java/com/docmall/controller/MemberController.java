package com.docmall.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.LoginDTO;
import com.docmall.domain.MemberVO;
import com.docmall.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

//final 필드만 매개변수가 있는 생성자를 만들어주고 스프링에 의하여 생성자 주입을 받게 된다.

@Log4j
@RequestMapping("/member/*")
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	// https://dev-coco.tistory.com/70
	
	//생성자를 통하여 주입받는 필드에 인터페이스를 사용하는 이유? 유지보수
	
	private final MemberService memberService;
	
	private final PasswordEncoder passwordEncoder;
	
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
	
	//회원정보 저장 -> 다른 주소 이동
	@PostMapping("/join")
	public String join(MemberVO vo, RedirectAttributes rttr) {
		
		log.info("회원정보 : " + vo);
		
		vo.setMbsp_password(passwordEncoder.encode(vo.getMbsp_password()));
		
		log.info("암호화된 비밀번호 : " + vo.getMbsp_password());
		
		memberService.join(vo);
		return "redirect:/member/login";
	}
	
	@GetMapping("/login")
	public void login() {
		
	}
	
	//로그인 인증확인 -> 메인페이지 (/) 주소이동 2)로그인 인증실패 -> 로그인폼 줌소
	//String mbsp_id, String mbsp_password
	@PostMapping("/login")
	public String login(LoginDTO dto, HttpSession session, RedirectAttributes rttr){
		
		log.info("로그인 : " + dto);
		
		MemberVO db_vo = memberService.login(dto.getMbsp_id());
		
		String url = "";
		String msg = "";
		
		// 아이디가 일치하는 경우
		if(db_vo != null) {
			//사용자가 입력한 비밀번호 (평뮨텍스트)와 db에서 가져온 비밀번호 암호화된 비밀번호 검사
			if(passwordEncoder.matches(dto.getMbsp_password(), db_vo.getMbsp_password())) {
				//로그인 성공결과를 세션형태로 저장
				session.setAttribute("loginStatus", db_vo);
				url = "/"; // 메인페이지 주소
			}else {
				url = "/member/login";
				msg = "비밀번호가 일치하지 않습니다.";
				rttr.addFlashAttribute("msg",msg); // 로그인폼 jsp 파일에서 사용 목적
			}
		}else {
			//아이디가 일치하지 않는 경우
			url = "/member/login";
			msg = "아이디가 일치하지 않습니다.";
			rttr.addFlashAttribute("msg",msg); // 로그인폼 jsp 파일에서 사용 목적
		}
		
		return "redirect:" + url;
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/";
	}
}
