package com.docmall.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CartVO;
import com.docmall.domain.MemberVO;
import com.docmall.dto.CartListDTO;
import com.docmall.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequiredArgsConstructor
@RequestMapping("/user/cart/*")
@Log4j
@Controller
public class CartController {

	private final CartService cartService;
	//메인 및 썸네일 이미지 업로드 폴더경로 주입작업
	@Resource(name="uploadPath") //servlet-context.xml bean id참조
	private String uploadPath;
	
	@ResponseBody
	@PostMapping("/cart_add")
	public ResponseEntity<String> cart_add(CartVO vo, HttpSession session) throws Exception{
		
		ResponseEntity<String> entity = null;
		
		//ajax방식에서 상품코드, 수량 2개정보만 전송되어옴. (로그인한 사용자의 아이디정보 추가작업)
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		vo.setMbsp_id(mbsp_id);
		
		cartService.cart_add(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		 
		return entity;
	}
	
	//장바구니 목록
	@GetMapping("/cart_list")
	public void cart_list(HttpSession session, Model model) throws Exception {
		
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		
		List<CartListDTO> cart_list = cartService.cart_list(mbsp_id);
		
		double cart_total_price = 0;
		/*
		 * //날짜 폴더의 역슬래시를 슬래시로 바꾸는 작업. 역슬래시로 되어있는 정보가 스프링으로 보내는 요청데이터에 사용되면 에러발생
		 * cart_list.forEach(vo -> {
		 * vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		 * 
		 * cart_total_price += (vo.getPro_price() - (vo.getPro_price() *
		 * (vo.getPro_discount() * 1/100))) * vo.getCart_amount(); });
		 */
		for(int i=0; i<cart_list.size(); i++) {
			CartListDTO vo = cart_list.get(i);
			
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
			cart_total_price += ((double)vo.getPro_price() - (vo.getPro_price() * (vo.getPro_discount() * 1/100))) * (double)vo.getCart_amount();
			
		}
		
		
		
		model.addAttribute("cart_list", cart_list);
		model.addAttribute("cart_total_price", cart_total_price);
		
	}

	//장바구니 목록 이미지 표시
	@ResponseBody
	@GetMapping("/imageDisplay") // /user/cart/imageDisplay 가 <img src="매핑주소">에 삽입
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception{
		
		return FileUtils.getFile(uploadPath + dateFolderName,fileName);
	}
	
	//장바구니 수량 변경
	@PostMapping("/cart_amount_change")
	public ResponseEntity<String> cart_amount_change(Long cart_code, int cart_amount) throws Exception{
		
		ResponseEntity<String> entity = null;
		
		cartService.cart_amount_change(cart_code, cart_amount);
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
}
