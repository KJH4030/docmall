package com.docmall.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.MemberVO;
import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.CartListDTO;
import com.docmall.kakaopay.ApproveResponse;
import com.docmall.kakaopay.ReadyResponse;
import com.docmall.service.CartService;
import com.docmall.service.KakaoPayServiceImpl;
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
	private final KakaoPayServiceImpl kakaoPayServiceImpl;
	
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
	
	//카카오페이 결제선택을 진행했을 경우
	//결제준비 요청
	@GetMapping(value="/orderPay", produces = "application/json")
	public @ResponseBody ReadyResponse payReady(String paymethod, OrderVO o_vo , /*OrderDetailVO od_Vo, PaymentVO p_vo, */int totalprice, HttpSession session) throws Exception{
		
		//log.info("결제방법 : " + paymethod);
		//log.info("주문 정보 : " + o_vo);

		//1)주문정보구성
		//	-주문테이블(OrderVO) : odr_status, payment_status 정보존재안함
		//	-주문상세테이블(OrderDetailVO) :
		//		- 장바구니테이블에서 데이터 참조 
		//	-결제테이블 : 보류
		//2)카카오페이 결제에 필요한 정보 구성		
		// 스프링에서 처리 가능한 부분		
		
		
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();//세션에서 아이디 호출
		o_vo.setMbsp_id(mbsp_id); //아이디 할당
		
		//시퀀스를 주문번호로 사용 : 동일한 주문번호 값이 사용.
		Long ord_code = (long) orderService.getOderSeq();
		o_vo.setOrd_code(ord_code); //주문번호 저장
		
		o_vo.setOrd_status("a");
		o_vo.setPayment_status("a");		
		

		
		List<CartListDTO> cart_list = cartService.cart_list(mbsp_id);
		
		String itemName = cart_list.get(0).getPro_name() +"외 " + String.valueOf(cart_list.size()-1) + " 건";
		
		// 1) 주문테이블 저장 odr_status, payment_status 데이터 준비 2)주문 상세 테이블 저장
		orderService.order_insert(o_vo);	
		
		// 3)Kakao pay 호출. I)결제준비 요청
		ReadyResponse readyResponse = kakaoPayServiceImpl.payReady(o_vo.getOrd_code(), itemName, cart_list.size(), mbsp_id, totalprice);
		
		log.info("결제 고유번호 : " + readyResponse.getTid());
		log.info("결제요청URL : " + readyResponse.getNext_redirect_pc_url());
		
		//카카오페이 결제승인요청작업에 필요한 정보준비
		session.setAttribute("tid", readyResponse.getTid());
		session.setAttribute("odr_code", o_vo.getOrd_code());
		
		return readyResponse;
	}
	
	//결제승인요청 작업. http://localhost:9090/user/order/orderApproval 
	@GetMapping("/orderApproval")
	public String orderApproval(@RequestParam("pg_token") String pg_token, HttpSession session) {
		
		
		// II)결제 승인 요청 작업
		Long odr_code = (Long) session.getAttribute("odr_code");
		String tid = (String) session.getAttribute("tid");
		String mbsp_id = ((MemberVO) session.getAttribute("loginStatus")).getMbsp_id();
		
		ApproveResponse approveResponse = kakaoPayServiceImpl.payApprove(odr_code, tid, pg_token, mbsp_id);
		
	
		
		
		session.removeAttribute("odr_code");
		session.removeAttribute("tid");
		
		return "redirect:/user/order/orderComplete";
		
		
	}
	
	// 결제 완료 페이지 
	@GetMapping("/orderComplete")
	public void orderComplete() {
		
	}	
	
	// 결제 취소시 http://localhost:9090/user/order/orderCancel
	@GetMapping("/orderCancel")
	public void orderCancel() {
		
	}
	// 결제 실패시 http://localhost:9090/user/order/orderFail
	@GetMapping("/orderFail")
	public void orderFail() {
		
	}
}
