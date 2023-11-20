<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.101.0">
    <title>Pricing example · Bootstrap v4.6</title>

    <!-- Bootstrap core CSS -->
<%@include file="/WEB-INF/views/comm/plugin2.jsp" %>



    <!-- Favicons -->


    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>

    <script>
      let msg = '${msg}';
      if(msg == 'success') {
        alert("회원정보가 수정됨");
      }
    </script>

  </head>
  <body>
    
<%@include file="/WEB-INF/views/comm/header.jsp" %>

<div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
  <h1>장바구니</h1>  
</div>

<div class="container">
	<table class="table table-striped">
		<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="checkAll"></th>
				<th scope="col">상품</th>
				<th scope="col">상품명</th>
				<th scope="col">판매가</th>
				<th scope="col">수량</th>
				<!-- <th scope="col">할인</th> -->
				<th scope="col">합계</th>
				<th scope="col">비고</th>
			</tr>
		</thead>
		<tbody>		
  		<c:forEach items="${cart_list }" var="cartListDTO">
			<tr>
				<th scope="row"><input type="checkbox" name="cart_code" value="${cartListDTO.cart_code }"></th>
				<td><img width="70" height="50" src="/user/cart/imageDisplay?dateFolderName=${cartListDTO.pro_up_folder }&fileName=${cartListDTO.pro_img}"></td>				
				<td>${cartListDTO.pro_name }</td>
				<td><span id="unitPrice">${cartListDTO.pro_price }</span></td>
				<td><input type="number" value="${cartListDTO.cart_amount }" name="cart_amount" style="width:40px;"> <button type="button" class="btn btn-info" name="btn_cart_amount_change" style="width:4em; height:2.5em; text-align:center;">변경</button></td>
				<%-- <td><span id="unitDiscount">${cartListDTO.pro_discount }</span>%</td> --%>
				<td><span class="unitTotalPrice" id="unitTotalPrice">${(cartListDTO.pro_price * cartListDTO.cart_amount) }</span></td>
				<td><button class="btn btn-danger" name="btn_cart_del" value="삭제">삭제(ajax)</button></td>
				<td><button class="btn btn-danger" name="btn_cart_del2" value="삭제">삭제(non_ajax)</button></td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="12"><button type="button" name="btn_cart_del" id="btn_cart_del" class="btn btn-danger">선택 삭제</button></td>
					
			
			</tr>
			<tr>
				<td colspan="12" style="text-align: right;">
					최종결제금액 : <span id="cart_total_price"><fmt:formatNumber type="currency" pattern="₩#,###원" value="${cart_total_price }">                		
                	</fmt:formatNumber></span>
				</td>			
			</tr>
			<tr>
				<td colspan="12" style="text-align: center;">
					<button class="btn btn-primary" id="btn_product">쇼핑 계속하기</button>
					<button class="btn btn-primary" id="btn_cart_empty">장바구니 비우기</button>
					<button class="btn btn-primary" id="btn_order">주문하기</button>
				</td>			
			</tr>
		</tfoot>
	</table>
  <%@include file="/WEB-INF/views/comm/footer.jsp" %>
</div>

<%@include file="/WEB-INF/views/comm/plugin.jsp" %>
	<!-- 카테고리 메뉴 자바스크립트 작업소스 -->
	<script src="/js/category_menu.js"></script>
	<script>


	$(document).ready(function(){

		//장바구니 목록에서 변경 클릭 시
		$("button[name='btn_cart_amount_change']").on("click", function(){

			let cur_btn_change = $(this); //현재 선택된 버튼태그의 위치를 참조
			
			let cart_amount = $(this).parent().find("input[name='cart_amount']").val();
			//console.log(cart_amount);

			let cart_code = $(this).parent().parent().find("input[name='cart_code']").val();
			//console.log(cart_code);

			$.ajax({
				url:'/user/cart/cart_amount_change',
				type:'post',
				data:{cart_code: cart_code, cart_amount : cart_amount},
				dataType: 'text',
				success : function(result){
					if(result == "success"){

						alert('변경이 완료되었습니다');
						//합계금액 계산작업
						// 금액 = (판매가 - (판매가 * 할인율)) * 수량
						let unitPrice = cur_btn_change.parent().parent().find("span#unitPrice").text();
						/* let unitDiscount = cur_btn_change.parent().parent().find("span#unitDiscount").text(); */

						//장바구니 코드별 단위 금액
						let unitTotalPrice = cur_btn_change.parent().parent().find("span#unitTotalPrice");
						console.log(unitTotalPrice);	
						unitTotalPrice.text(unitPrice * cart_amount);						
						
						console.log(unitTotalPrice);	
						
			
						//전체 주문 금액
						fn_cart_sum_price();
						
					}
				}

				
			});
		});

		//장바구니 삭제(ajax 사용)
		$("button[name='btn_cart_del']").on("click", function(){

			if(!confirm("장바구니 상품을 삭제하시겠습니까?")) return;
			
			let cur_btn_delete = $(this); //현재 선택된 버튼태그의 위치를 참조. ajax사용 시 this인식이 안되기 떄문에 미리 담아둔다
			let cart_code = $(this).parent().parent().find("input[name='cart_code']").val();
			//console.log("장바구니 코드 : "+ cart_code);

			$.ajax({

				url: '/user/cart/cart_list_del',
				type: 'post',
				data: {cart_code : cart_code},
				dataType: 'text',
				success: function(result){
					if(result == "success"){
						alert("삭제 되었습니다.")
	
						cur_btn_delete.parent().parent().remove(); //삭제된 장바구니 데이터행 제거

						//전체주문금액
						fn_cart_sum_price();
					}
				}

			});

		});

		//장바구니 삭제 (non_ajax)
		$("button[name='btn_cart_del2']").on("click", function(){
			if(!confirm("장바구니 상품을 삭제하시겠습니까?")) return;
			
			let cart_code = $(this).parent().parent().find("input[name='cart_code']").val();

			location.href = "/user/cart/cart_list_del?cart_code=" + cart_code; //get방식

			fn_cart_sum_price();

		});

		//주문정보 페이지
		$("button#btn_order").on("click", function(){

			location.href="/user/order/order_info";
			
		});
			
	}); // jquery ready end

	function fn_cart_sum_price(){


		//전체 주문 금액
		let sumPrice = 0;
		$(".unitTotalPrice").each(function(){
			sumPrice += Number($(this).text());
		});
		$("#cart_total_price").text(sumPrice);

	}
	
	</script>
    
  </body>
</html>
    