<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author"
	content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
<meta name="generator" content="Hugo 0.101.0">
<title>Pricing example · Bootstrap v4.6</title>

<!-- Bootstrap core CSS -->
<%@include file="/WEB-INF/views/comm/plugin2.jsp"%>



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

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}
</style>

<script>
	let msg = '${msg}';
	if (msg == 'success') {
		alert("회원정보가 수정됨");
	}
</script>

</head>
<body>

	<%@include file="/WEB-INF/views/comm/header.jsp"%>

	<div
		class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
		<h1>주문정보</h1>
	</div>

	<div class="container">
		
		
		<div class="box box-primary">
			<div class="box-body">
				<h3>카카오 결제 및 주문처리가 되었습니다.</h3>
			</div>
		</div>
		<%@include file="/WEB-INF/views/comm/footer.jsp"%>
	</div>
	
	
<!-- iOS에서는 position:fixed 버그가 있음, 적용하는 사이트에 맞게 position:absolute 등을 이용하여 top,left값 조정 필요 -->
<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;">
<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
</div>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // 우편번호 찾기 화면을 넣을 element
    var element_layer = document.getElementById('layer');

    function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function sample2_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    document.getElementById("sample2_extraAddress").value = extraAddr;
                
                } else {
                    document.getElementById("sample2_extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample2_postcode').value = data.zonecode;
                document.getElementById("sample2_address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("sample2_detailAddress").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_layer.style.display = 'none';
            },
            width : '100%',
            height : '100%',
            maxSuggestItems : 5
        }).embed(element_layer);

        // iframe을 넣은 element를 보이게 한다.
        element_layer.style.display = 'block';

        // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
        initLayerPosition();
    }

    // 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
    // resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
    // 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
    function initLayerPosition(){
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        // 위에서 선언한 값들을 실제 element에 넣는다.
        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';
        // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
    }
</script>

<%@include file="/WEB-INF/views/comm/plugin.jsp"%>
<!-- 카테고리 메뉴 자바스크립트 작업소스 -->
<script src="/js/category_menu.js"></script>
<script>
	$(document).ready(function() {

		//수령인과 동일
		$("#same").on("click", function(){

			if($("#same").is(":checked")){
				//console.log("체크");

				$("#mbsp_name").val($("#b_mbsp_name").val());
				$("#sample2_postcode").val($("#b_mbsp_zipcode").val());
				$("#sample2_address").val($("#b_mbsp_addr").val());
				$("#sample2_detailAddress").val($("#b_mbsp_deaddr").val());
				$("#mbsp_phone").val($("#b_mbsp_phone").val());
			}else{

				$("#mbsp_name").val("");
				$("#sample2_postcode").val("");
				$("#sample2_address").val("");
				$("#sample2_detailAddress").val("");
				$("#mbsp_phone").val("");

			}

		});

		//주문하기
		$("#btn_order").on("click", function(){

		//1)주문테이블, 주문상세테이블, 결제테이블에 필요한 정보 구성
		//2)카카오페이 결제에 필요한 정보 구성
		
		// 스프링에서 처리 가능한 부분
		/* 
		console.log("paymethod : ", $("input[name='paymethod']:checked").val());
		console.log("ord_name", $("#mbsp_name").val());
		console.log("ord_zipcode", $("input[name='mbsp_zipcode']").val());
		console.log("ord_addr_basic", $("input[name='mbsp_addr']").val());
		console.log("ord_addr_detail", $("input[name='mbsp_deaddr']").val());
		console.log("ord_tel", $("#mbsp_phone").val());
		console.log("ord_price", $("#cart_total_price").text()); 
		*/
		
		$.ajax({

			url: '/user/order/orderPay',
			type: 'get',
			data:{
				paymethod : $("input[name='paymethod']:checked").val(),
				ord_name: $("#mbsp_name").val(),
				ord_zipcode: $("input[name='mbsp_zipcode']").val() ,
				ord_addr_basic: $("input[name='mbsp_addr']").val(),
				ord_addr_detail: $("input[name='mbsp_deaddr']").val(),
				ord_tel: $("#mbsp_phone").val(),
				ord_price: 10,//$("#cart_total_price").text(),
				totalprice: 10//$("#cart_total_price").text()
			},
			dataType : 'json',
			success : function(response){

				console.log("응답 : " + response);

				alert(response.next_redirect_pc_url);
				location.href = response.next_redirect_pc_url;
				
			}
		})



		});

	}); // jquery ready end
</script>

</body>
</html>
