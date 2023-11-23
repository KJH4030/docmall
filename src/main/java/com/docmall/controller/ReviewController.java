package com.docmall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docmall.domain.MemberVO;
import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/user/review/*")
@RequiredArgsConstructor
@RestController
public class ReviewController {

	private final ReviewService reviewService;
	
	
	//@RequestBody : JSON 데이터를 ReviewVO객체로 변환해주는 기능.. 참고> 반대의미 어노테이션 @ResponseBody
	@PostMapping(value="/new", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> review_insert(@RequestBody ReviewVO vo, HttpSession session) throws Exception{
		
		log.info("리뷰 : " + vo);
		
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		vo.setMbsp_id(mbsp_id);		
		
		ResponseEntity<String> entity = null;
		
		reviewService.review_insert(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
								
		return entity;
		
	}
	
	// 전통적인 형태의 주소 list?pro_num=10&page=1 -> Rest API개발형태의 주소 list/10/1
	@GetMapping("/list/{pro_num}/{page}")
	public ResponseEntity<Map<String, Object>> list(@PathVariable("pro_num") Integer pro_num,@PathVariable("page") Integer page ) throws Exception{
		
		//리턴타입에 다른 구문
		// - select문
		//ResponseEntity<Map<String, Object>> : 1)상품후기 목록 데이터 List<ReviewVO> 2)페이징 데이터 PageDTO
		//ResponseEntity<List<ReviewVO>> : 상품후기목록 데이터 - List<ReviewVO>
		//ResponseEntity<PageDTO> : 페이징 데이터 - PageDTO
		
		// - insert, delete, update
		//ResponseEntity<String>
		
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		//1)상품후기 데이터 목록 작업
		Criteria cri = new Criteria();
		//cri.setAmount(20);
		cri.setPageNum(page);
		
		//db연동.
		List<ReviewVO> list = reviewService.list(pro_num, cri);
		
		//2)페이징 데이터 작업
		int listCount = reviewService.listCount(pro_num);
		PageDTO pageMaker = new PageDTO(cri,listCount);
		
		map.put("list", list); // 상품후기목록 데이터 - List<ReviewVO>
		map.put("pageMaker", pageMaker); // 페이징 데이터 - PageDTO
		
		// jackson-databind 라이브러리에 의하여 map -> json으로 변환되어 ajax 호출
		entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
		return entity;
	}
	
}