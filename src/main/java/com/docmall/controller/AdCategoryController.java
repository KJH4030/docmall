package com.docmall.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CategoryVO;
import com.docmall.service.AdCategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/admin/category/**")
@RequiredArgsConstructor
@Controller //ajax호출 또는 일반호출이 함께 사용하는 경우
//@RestController (@Controller + @ResponseBody): 모든 매핑주소가 ajax호출로 사용하는 경우
public class AdCategoryController {

	private final AdCategoryService adCategoryService;
	
	//1차카테고리 선택에 따른 2차카테고리 정보를 클라이언트에게 제공.
	//일반주소 secondCategory?cg_parent_code=1
	//RestFull 개발론 주소 secondCategory/1
	//주소의 일부분을 값으로 사용하고자 할 경우 {} 사용.
	@ResponseBody //@Controller 어노테이션을 쓰는 경우 AJAX로 사용될 때
	@GetMapping("/secondCategory/{cg_parent_code}")
	public ResponseEntity<List<CategoryVO>> secondCategory(@PathVariable("cg_parent_code") Integer cg_parent_code) throws Exception{
		
		log.info("1차 카테고리 코드 : " + cg_parent_code);
		ResponseEntity<List<CategoryVO>> entity = null;

		entity = new ResponseEntity<List<CategoryVO>>(adCategoryService.getSecondCategoryList(cg_parent_code), HttpStatus.OK);

		//List<CategoryVO> list = adCategoryService.getSecondCategoryList(cg_parent_code);
		//list객체 -> JSON으로 변환하는 라이브러리. Jackson-databind 라이브러리:pom.xml참고
		
		return entity;
	}
	
}
