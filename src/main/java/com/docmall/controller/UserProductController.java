package com.docmall.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.UserProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequestMapping("/user/product/*")
@RequiredArgsConstructor
@Log4j
@Controller
public class UserProductController {

	private final UserProductService userProductService;
	
	//메인 및 썸네일 이미지 업로드 폴더경로 주입작업
	@Resource(name="uploadPath") //servlet-context.xml bean id참조
	private String uploadPath;
	
	//매핑주소1: /user/product/pro_list?cg_code=2차카테고리 코드
	/*
	@GetMapping("/pro_list")
	public void pro_list(@RequestParam("cg_code") Integer cg_code) throws Exception{
		
	}
	*/
	//매핑주소2: /user/product/pro_list/2차카테고리 코드 (REST API 개발형태)
	@GetMapping("/pro_list/{cg_code}/{cg_name}")
	public String pro_list(Criteria cri, @PathVariable("cg_code") Integer cg_code, @PathVariable("cg_name") String cg_name, Model model) throws Exception{
		

		cri.setAmount(4);
		
		List<ProductVO> pro_list = userProductService.pro_list(cg_code, cri);
		
		//날짜 폴더의 역슬래시를 슬래시로 바꾸는 작업. 역슬래시로 되어있는 정보가 스프링으로 보내는 요청데이터에 사용되면 에러발생
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		model.addAttribute("pro_list", pro_list);
		
		int totalCount = userProductService.getTotalCount(cg_code);
		model.addAttribute("pageMaker", new PageDTO(cri,totalCount));	
		
		return "/user/product/pro_list";
	}
	

	@ResponseBody
	@GetMapping("/imageDisplay") // /admin/product/imageDisplay 가 <img src="매핑주소">에 삽입
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception{
		
		return FileUtils.getFile(uploadPath + dateFolderName,fileName);
	}
}
