package com.docmall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.AdOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequiredArgsConstructor
@RequestMapping("/admin/order/*")
@Log4j
@Controller
public class AdOrderController {

	private final AdOrderService adOrderService;
	
	//메인 및 썸네일 이미지 업로드 폴더경로 주입작업
	@Resource(name="uploadPath") //servlet-context.xml bean id참조
	private String uploadPath;
	
	
	//상품리스트. (목록과페이징)
	@GetMapping("/order_list")
	public void pro_list(Criteria cri, Model model) throws Exception{
		
		//this(1,10);
		// 10->2 페이지당 글 표시 갯수
		cri.setAmount(5);
		
		List<OrderVO> order_list = adOrderService.order_list(cri);		
		model.addAttribute("order_list", order_list);
		
		int totalCount = adOrderService.getTotalCount(cri);
		model.addAttribute("pageMaker", new PageDTO(cri,totalCount));				
	}
	
	@GetMapping("/order_detail_info/{ord_code}")
	public ResponseEntity<List<OrderDetailVO>> list(@PathVariable("pro_num") Integer pro_num, @PathVariable("page") Integer page) throws Exception {
						
		// 클래스명 = 주문상세 + 상품테이블 조인한 결과
		ResponseEntity<List<OrderDetailVO>> entity = null;
		
		return entity;
	}
	
	
	
	//상품 리스트에서 보여줄 이미지. <img src="매핑주소"> 리턴받는것들은 반드시 @ResponseBody
	@ResponseBody
	@GetMapping("/imageDisplay") // /admin/product/imageDisplay 가 <img src="매핑주소">에 삽입
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception{
		
		return FileUtils.getFile(uploadPath + dateFolderName,fileName);
	}
}
