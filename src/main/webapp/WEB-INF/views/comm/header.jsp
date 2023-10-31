<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom shadow-sm">
  <h5 class="my-0 mr-md-auto font-weight-normal"><a href="/">DOCMALL</a></h5>
  <c:if test="${sessionScope.loginStatus != null }">
    <div>
      <b>${sessionScope.loginStatus.mbsp_id }님</b>
      <b>${sessionScope.loginStatus.mbsp_email }님</b>      
      <b>${sessionScope.loginStatus.mbsp_lastlogin }</b>
    </div>
  </c:if>
  <nav class="my-2 my-md-0 mr-md-3">
  
  	<!-- 로그인 이전 표시 -->
  	<c:if test="${sessionScope.loginStatus == null}">
	    <a class="p-2 text-dark" href="/member/login">Login</a>
	    <a class="p-2 text-dark" href="/member/join">Join</a>
    </c:if>
    
    <!-- 로그인 이후 표시 -->
    <c:if test="${sessionScope.loginStatus != null}">
	    <a class="p-2 text-dark" href="/member/logout">Logout</a>
	    <a class="p-2 text-dark" href="/member/confirmPw">modify</a>    
    </c:if>
    <a class="p-2 text-dark" href="/member/mypage">MyPage</a>
    <a class="p-2 text-dark" href="#">Order</a>
    <a class="p-2 text-dark" href="#">Cart</a>
  </nav>
</div>