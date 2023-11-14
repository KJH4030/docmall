package com.docmall.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.AdCategoryService;
import com.docmall.service.AdProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;


@Controller
@Log4j
@RequestMapping("/admin/product/*")
@RequiredArgsConstructor
public class AdProductController {

	private final AdProductService adProductService;
	private final AdCategoryService adCategoryService;
	
	//메인 및 썸네일 이미지 업로드 폴더경로 주입작업
	@Resource(name="uploadPath") //servlet-context.xml bean id참조
	private String uploadPath;
	
	//CKEditor에서 사용되는 업로드 폴더 경로
	@Resource(name="uploadCKPath") //servlet-context.xml bean id참조
	private String uploadCKPath;
	
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
		String dateFolder = FileUtils.getDateFolder();
		String savedFileName = FileUtils.uploadFile(uploadPath, dateFolder, uploadFile);
		
		vo.setPro_img(savedFileName);
		vo.setPro_up_folder(dateFolder);
		
		log.info("상품정보 : " + vo);
		
		//상품정보를 DB에 저장
		adProductService.pro_insert(vo);
		
		return "redirect:/admin/product/pro_list"; //상품리스트 주소 이동
	}
	
	//CkEditor 업로드 탭에서 파일업로드시 동작하는 매핑주소
	//MultipartFile upload 변수와 CkEditor의 <input type="file" name="upload" size="38">일치
	//MultipartFile upload : 업로드 된 파일을 참조하는 객체
	//HttpServletRequest 클래스 : jsp의 request객체 클래스. 클라이언트의 요청을 담고있는 객체
	//HttpServletResponse 클래스 : jsp의 response객체 클래스. 클라이언트로 보낼 서버측의 응답정보를 가지고있는 객체.

	@PostMapping("imageUpload")
	public void imageUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) {
		
		//try 코드 사용 전에 선언한 목적 
		OutputStream out = null;
		PrintWriter printWriter = null; //클라이언트에 서버의 응답정보를 보낼 때 사용
		
		//jsp파일
		/*
		 * <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		 * 
		 */
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		try {
			String fileName = upload.getOriginalFilename(); // 클라이언트에서 전송한 파일이름
			
			byte[] bytes = upload.getBytes(); //업로드 한 파일을 byte배열로 읽어들임
			
			String ckUploadPath = uploadCKPath + fileName;
			
			log.info("CKEditor 파일 경로 : " + ckUploadPath);
			
			out = new FileOutputStream(new File(ckUploadPath)); //0kb파일 생성
			
			out.write(bytes); //출력스트림 작업
			out.flush();
			
			//2) 파일업로드 작업 후 파일정보를 CKEditor로 보내는 작업
			printWriter = response.getWriter();
			
			// 브라우저의 CKEditor에서 사용할 업로드한 파일 정보를 참조하는 경로설정
			// 1)톰캣 Context Path에서 Add External Web Module 작업을 해야 함.
			// Path : /upload, Document Base : C:\\dev\\upload\\ckeditor 설정
			// 2)Tomcat server.xml에서 <Context docBase="업로드경로" path="매핑주소" reloadable="true"/>
			// <Context docBase="C:\\dev\\upload\\ckeditor" path="/ckupload" reloadable="true"/>
			 
			//CKeditor에서 업로드 된 파일경로를 보내준다.(매핑주소와 파일명이 포함)
			String fileUrl = "/ckupload/" + fileName;
			// {"filename":"abc.gif", "uploaded":1, "url":"/upload/abc.gif"}
			printWriter.println("{\"filename\":\"" +  fileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}");
			printWriter.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				}catch(Exception ex) {
					ex.printStackTrace();					
				}
			}
			if(printWriter != null) printWriter.close();
		}
		
	}
	
	//상품리스트. (목록과페이징)
	@GetMapping("/pro_list")
	public void pro_list(Criteria cri, Model model) throws Exception{
		
		//this(1,10);
		// 10->2 페이지당 글 표시 갯수
		cri.setAmount(2);
		
		List<ProductVO> pro_list = adProductService.pro_list(cri);
		
		//날짜 폴더의 역슬래시를 슬래시로 바꾸는 작업. 역슬래시로 되어있는 정보가 스프링으로 보내는 요청데이터에 사용되면 에러발생
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});		
		model.addAttribute("pro_list", pro_list);
		
		int totalCount = adProductService.getTotalCount(cri);
		model.addAttribute("pageMaker", new PageDTO(cri,totalCount));				
	}
	
	//상품 리스트에서 보여줄 이미지. <img src="매핑주소"> 리턴받는것들은 반드시 @ResponseBody
	@ResponseBody
	@GetMapping("/imageDisplay") // /admin/product/imageDisplay 가 <img src="매핑주소">에 삽입
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception{
		
		return FileUtils.getFile(uploadPath + dateFolderName,fileName);
	}
	
	//체크상품 목록 수정(ajax요청) 1번
	//일반요청시 배열형태로 파라미터가 전송되어오면, @RequestParam("pro_num_arr[]") []를 제외
	@ResponseBody
	@PostMapping("/pro_checked_modify1")
	public ResponseEntity<String> pro_checked_modify1(
			@RequestParam("pro_num_arr[]") List<Integer> pro_num_arr,
			@RequestParam("pro_price_arr[]") List<Integer> pro_price_arr,
			@RequestParam("pro_buy_arr[]") List<String> pro_buy_arr) throws Exception{
		
		log.info("상품코드 : " + pro_num_arr);
		log.info("상품가격 : " + pro_price_arr);
		log.info("판매여부 : " + pro_buy_arr);
		
		ResponseEntity<String> entity = null;
		
		//체크상품 수정작업
		adProductService.pro_checked_modify1(pro_num_arr, pro_price_arr, pro_buy_arr);		
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//체크박스 수정 2번
	@ResponseBody
	@PostMapping("/pro_checked_modify2")
	public ResponseEntity<String> pro_checked_modify2(
			@RequestParam("pro_num_arr[]") List<Integer> pro_num_arr,
			@RequestParam("pro_price_arr[]") List<Integer> pro_price_arr,
			@RequestParam("pro_buy_arr[]") List<String> pro_buy_arr) throws Exception{
		
		log.info("상품코드 : " + pro_num_arr);
		log.info("상품가격 : " + pro_price_arr);
		log.info("판매여부 : " + pro_buy_arr);
		
		
		
		ResponseEntity<String> entity = null;
		
		//체크상품 수정작업
		adProductService.pro_checked_modify1(pro_num_arr, pro_price_arr, pro_buy_arr);		
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//상품수정 폼 페이지
	@GetMapping("/pro_edit")
	public void pro_edit(@ModelAttribute("cri") Criteria cri, Integer pro_num, Model model) throws Exception {		
		
		//선택한 상품 정보
		ProductVO productVO = adProductService.pro_edit(pro_num);
		// 역슬래시를 슬래시로 변환 작업. ( \ -> / )
		productVO.setPro_up_folder(productVO.getPro_up_folder().replace("\\", "/"));
		
		model.addAttribute("productVO",productVO);
		//log.info("로그 확인 : " + productVO);
		
		//1차 카테고리 GlobalControllerAdvice 클래스 Model 참조
		
		//상품카테고리에서 2차카테고리를 이요한 1차카테고리 정보를 참조.
		//productVO.getCg_code() : 상품테이블에 들어 있는 2차카테고리 코드
		CategoryVO firstCategory = adCategoryService.get(productVO.getCg_code());
		model.addAttribute("first_category",firstCategory);
		
		//1차 카테고리를 부모로 둔 2차카테고리 정보.
		//현재 상품의 1차카테고리 코드 : firstCategpry.getCg_code();
		model.addAttribute("second_categoryList",adCategoryService.getSecondCategoryList(firstCategory.getCg_parent_code()));
		
				
	}
	
	//상품수정 기능
	@PostMapping("/pro_edit")
	public String pro_edit(Criteria cri, ProductVO vo, MultipartFile uploadFile, RedirectAttributes rttr) throws Exception {
		
		//상품리스트에서 사용할 정보(검색,페이징정보)
		log.info("검색페이징 정보 : " + cri);
		//상품수정내용
		log.info("상품수정내용 : " + vo);
		
		vo.setPro_up_folder(vo.getPro_up_folder().replace("/", "\\"));
		
		//작업
		//파일이 변경 될 경우 해야 할 작업
		//1) 기존 이미지파일 삭제 2) 업로드 작업
		//참고 > 클라이언트 파일명을 db에 저장하는 부분..
		//첨부파일 확인할 때 사용 조건식 uploadFile.getSize() > 0
		if(!uploadFile.isEmpty()) {
			
			//1)기존이미지파일 삭제 작업
			FileUtils.deleteFile(uploadPath, vo.getPro_up_folder(), vo.getPro_img());
			
			//2)업로드작업
			String dateFolder = FileUtils.getDateFolder();
			String savedFileName = FileUtils.uploadFile(uploadPath, dateFolder, uploadFile);
			
			//3)db에 저장한 새로운 날짜폴더명 및 이미지
			vo.setPro_img(savedFileName);
			vo.setPro_up_folder(dateFolder);
		}
		
		//db연동작업
		adProductService.pro_edit(vo);
		
		
		return "redirect:/admin/product/pro_list" + cri.getListLink();
	}
	
	@PostMapping("/pro_delete")
	public String pro_delete(Criteria cri, Integer pro_num) throws Exception{
		
		// db연동 작업
		adProductService.pro_delete(pro_num);
		
		return "redirect:/admin/product/pro_list" + cri.getListLink();
	}
	
}
