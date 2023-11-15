package com.docmall.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.domain.MemberVO;
import com.docmall.dto.CartListDTO;
import com.docmall.service.CartService;
import com.docmall.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@RequestMapping("/user/order/*")
@Controller
public class OrderController {

	private final CartService cartService;
	private final OrderService orderService;
	
	//주문정보페이지
	@GetMapping("/order_info")
	public void order_info(HttpSession session, Model model) throws Exception{
	
		// 주문정보목록
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		
		List<CartListDTO> order_info = cartService.cart_list(mbsp_id);
		
		double order_price = 0;
		/*
		 * //날짜 폴더의 역슬래시를 슬래시로 바꾸는 작업. 역슬래시로 되어있는 정보가 스프링으로 보내는 요청데이터에 사용되면 에러발생
		 * cart_list.forEach(vo -> {
		 * vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		 * 
		 * cart_total_price += (vo.getPro_price() - (vo.getPro_price() *
		 * (vo.getPro_discount() * 1/100))) * vo.getCart_amount(); });
		 */
		for(int i=0; i<order_info.size(); i++) {
			
			CartListDTO vo = order_info.get(i);
			
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
			order_price += ((double)vo.getPro_price() - (vo.getPro_price() * (vo.getPro_discount() * 1/100))) * (double)vo.getCart_amount();
			
		}		
		
		model.addAttribute("order_info", order_info);
		model.addAttribute("order_price", order_price);
		
	}	
}
