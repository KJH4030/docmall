<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.101.0">
    <title>Pricing example · Bootstrap v4.6</title>    

    

<%@ include file="/WEB-INF/views/comm/plugin2.jsp" %>

<!-- header -->
<%@ include file="/WEB-INF/views/comm/header.jsp" %>

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
        if(msg != ""){
            alert(msg);
        }
    </script>
  </head>
  <body>    

<div class="container">
  <div class="text-center">
	<div class="box box-primary">
		<div class="box-header with-border">
			<h3 class="box-title">로그인</h3>
		</div>
    <form role="form" id="loginForm" method="post" action="/member/login" >
      <div class="box-body">
        <div class="form-group row">
            <label for="mbsp_id" class="col-2">아이디</label>
            <div class="col-10">
            <input type="text" class="form-control" id="mbsp_id" name="mbsp_id" placeholder="아이디 입력">
            </div>
        </div>            
        <div class="form-group row">
            <label for="mbsp_password" class="col-2">비밀번호</label>
            <div class="col-10">
            <input type="password" class="form-control" id="mbsp_password" name="mbsp_password" placeholder="비밀번호 입력">
            </div>
        </div>     
      </div>
      
      <div class="box-footer">
      <button type="submit" class="btn btn-primary" id="btnLogin">로그인</button>
      </div>
      </form>
  </div>
	</div>

	<!-- footer -->
  <%@ include file="/WEB-INF/views/comm/footer.jsp" %>
  
</div> 

<%@ include file="/WEB-INF/views/comm/plugin.jsp" %>

<script>

  let useIDCheck = false;

	//jquery.slim.min.js 파일에 jquery 명령어가 정의되어 있음
	// 별칭 : $ -> jQuery()함수
	//ready()이벤트 메소드 : 브라우저가 html 태그를 모두 읽고 난 후 동작하는 특징.
	$(document).ready(function(){
		// document.getElementById("idCheck");
		// $("CSS 선택자")
		$("#idCheck").click(function() {
			//alert("아이디 중복체크");
			if($("#mbsp_id").val() == ""){
				alert("아이디를 입력하세요.");
				$("#mbsp_id").focus();
				return;
			}
			//아이디 중복체크
			$.ajax({
				url : '/member/idCheck',
				type : 'get',
				datatype : 'text',
				data : {mbsp_id : $("#mbsp_id").val()}, //서버에 보낼 데이터
				success : function(result){
					if(result == "yes"){
						alert("사용 가능한 아이디 입니다.");
						useIDCheck = true;      
					}else{
						alert("사용 불가능한 아이디 입니다.");
						useIDCheck = false;
						$("#mbsp_id").val(""); //아이디 텍스트 박스 초기화 
						$("#mbsp_id").focus();						
					}
				}
			});
		});

		// 메일인증 요청
    $("#mailAuth").click(function() {

      //if($("메일입력텍스트박스태그"))
      if($("#mbsp_email").val() == ""){
        alert("이메일을 입력하세요.");
        $("#mbsp_email").focus();
        return;				
      }

      $.ajax({
        url: '/email/authcode',
        type : 'get',
        datatype : 'text', 
        data : {receiverMail : $("#mbsp_email").val()},
        success : function(result){
          if(result == "success"){
            alert("인증메일이 발송되었습니다. 확인 바랍니다.")
          }else{
            
          }
        } 
      });
    });
    let isConfirmAuth = false; // 메일 인증을 받지 않은 상태
    // 인증확인
    $("#btnComfirmAuth").click(function(){

      if($("#authCode").val() == ""){
        alert("인증번호를 입력해주세요")
        $("#authCode").focus();
        return;
      }

      

      //인증확인 요청
      $.ajax({
        url: '/email/confirmAuthcode',
        type : 'get',
        dataType : 'text',
        data : {authCode : $("#authCode").val()},
        success : function(result){
          if(result == "success"){
            alert("인증 성공");
            isConfirmAuth = true;
          }else if(result == "fail"){
            alert("인증번호를 확인해주세요");
            $("#authCode").val("");
            $("#authCode").focus();
            isConfirmAuth = false;
          }else if(result == "request"){
            alert("인증번호가 만료되었습니다.");
            $("#authCode").val("");
            isConfirmAuth = false;
          }
        }
      });
    });

    //from 참조 <form role="form" id="joinForm" method="post" action="" >
    let joinForm =$("#joinForm");

    //로그인 버튼
    // $("#btnLogin").click(function(){

  	// 	joinForm.submit();
    // });
});
	
</script>    
  </body>
</html>
    