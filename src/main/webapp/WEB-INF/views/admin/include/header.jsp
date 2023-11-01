<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
  <header class="main-header">

    <!-- Logo -->
    <a href="index2.html" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><b>A</b>LT</span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>Admin </b>DocMall</span>
    </a>

    <!-- Header Navbar -->
    <nav class="navbar navbar-static-top" role="navigation">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      <!-- Navbar Right Menu -->
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li>
              <a href="/">[DocMall]</a>
          </li>
          <li>
              <a href="#">최근접속시간 : [<fmt:formatDate value="${sessionScope.adminStatus.admin_visit_date }" pattern="yyyy-MM-dd hh시-mm분-ss초"/>]</a>
          </li>
          <li>
  			<c:if test="${sessionScope.adminStatus != null}">
            <a href="/admin/logout">            	
              <span>로그아웃</span>
            </a>
            </c:if>            
          </li>
         </ul>
      </div>
    </nav>
  </header>