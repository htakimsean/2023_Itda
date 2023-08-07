<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(function(){
		$("#joinForm").click(function(){
			location.href = "join.me";
			
		})
		
		
		const userId = '${userId}';
		
		if (userId) {								//id값이 있는 경우
			$("#userId").val(userId);				//아이디 입력란에 자동으로 id를 채워줌
			$("#remember").prop('checked',true);	//자동으로 아이디 기억하기를 체크해줌
		}
		
	});
</script>
<div class="backyard">
<nav id="navbody">
	<!-- 왼쪽 상단 로고 링크 -->
	<a id="logo-brand" href="main.jsp"><img src="../image/common/itda_logo.png" style="width: 100px; height: auto;"></a>	
	<ul id="right_btns">
    	<li id="search">
	      	<a id="search_btn" onclick="location.href='search.jsp'">
		  		<img src="../image/common/search.png" style="width: 30px; height: auto;">
		  	</a>
		</li>
		
	<%
		String userId = (String) session.getAttribute("userId");
		if (userId != null && !userId.equals("")) {
	%>
	
		<!-- 로그인이 되어 있는 경우 프로필사진 출력 -->
		<div class="dropdown">
		  <button class="dropbtn"><img src="../image/common/profile.png" style="width: 30px; height: auto;"></button>
		  <div class="dropdown-content">
		    <a href="mypage.jsp">마이페이지</a>
		    <a href="logout.me">로그아웃</a>
		  </div>
		</div>
	<%
		}else {
	%>
	  
		<li id="login_modal">
      	<!-- 헤더의 로그인 버튼 -->
		<button type="button" class="login_btn" data-toggle="modal" data-target="#myModal"
		 		style="color:#FBD1A7; background: rgb(1, 39, 60); border: none;">로그인</button>
			
			<!-- Modal -->
			<form id="modalForm" action="loginProcess.me" method="post">
			<div class="modal fade" id="myModal">
				<div class="modal-dialog">
				<div class="modal-content" style="background: rgb(204, 204, 204);">
					<div class="modal-header" style="text-align: center; border: none;">
						<h3 class="modal-title" style="
						 border: none; text-align: center; margin:20px auto auto auto;">SIGN IN</h3>
					</div>
					
					<div class="modal-body">
				    	<p><br>
				    		<input type="text" name="userId" style="width:100%; height: 40px;
				    			border: none; background: rgb(204, 204, 204);
				    			border-bottom: 1px solid rgb(1, 39, 60) !important;
				    			outline: none;"
				    			placeholder="아이디"><br>
				    	</p>
						<p><br>
							<input type="text" name="userPw" style="width:100%; height: 40px;
				    			border: none; background: rgb(204, 204, 204);
				    			border-bottom: 1px solid rgb(1, 39, 60) !important;
				    			outline: none;"
				    			placeholder="비밀번호">
						</p>
						<div id="rememberbox">
						<label>
          					<input type="checkbox" name="remember" id="remember" value="store">아이디 기억하기
        				</label>
						</div>
						<div style="float: right">
							<a class="find_account" href="account.jsp"
								style="color: black; font-size: 8px;">아이디·비밀번호 찾기</a>
						</div><br><br>
						<div style="text-align:center; margin:auto;">
						<button type="submit" class="real_login_btn"
						 style="color: white; background: rgb(1, 39, 60); text-align:center;
						 width: 100%; height: auto; margin:auto;">로그인</button>
						</div>
					</div>
					
					<div class="modal-footer" style="text-align: center; border: none;">
						<a class="modal_join" style="color: black; disabled
								text-align: center; margin: auto 0px auto auto;">아이디가 없으신가요?</a>&nbsp;
						<a class="modal_join" href="join.jsp" id="joinForm" style="color: black;
							text-decoration: underline; text-align: center; margin: auto auto auto 0px;;">회원가입</a>
					</div>
				        
				</div>	<!-- modal-content end -->
				</div>	<!-- modal-dialog end -->
			</div>	<!-- Modal end -->
			</form>
		</li><!-- login_modal end -->
	<%
		}
	%>
	</ul>
</nav>
</div>