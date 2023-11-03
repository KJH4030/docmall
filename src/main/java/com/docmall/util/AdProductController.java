package com.docmall.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.docmall.domain.ProductVO;
import com.docmall.service.AdProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;


@Controller
@Log4j
@RequestMapping("/admin/product/*")
@RequiredArgsConstructor
public class AdProductController {

	private final AdProductService adProductService;
	
	//상품등록 폼
	@GetMapping("/pro_insert")
	public void pro_insert() {
		
		log.info("상품등록 폼..");
		
	}
	
	//파일이 여러개일 경우 List<MultipartFile> 감싸줘야 함
	//파일 업로드 기능 : 1)스프링에서 지원하는 기본 라이브러리 servlet-context.xml 에서 MultipartFile에 대한 bean 등록 필요
	//			   2)외부 라이브러리(pom.xml). servlet-context.xml 에서 MultipartFile에 대한 bean 등록 필요
	@PostMapping("/pro_insert")
	public String pro_insert(ProductVO vo, MultipartFile uploadFile) {
		
		log.info("상품정보 : " + vo);
		
		//파일업로드 작업. 선수작업: FileUtils 클래스 작업
		
		//상품정보를 DB에 저장
		
		return "redirect:/리스트";
	}
}
