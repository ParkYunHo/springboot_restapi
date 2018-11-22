<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>register client</title>
<%@ include file="bootstrap.jsp" %>
</head>
<body>
	<div class="container">
		<form method="post" id="registerForm">
			<div class="form-group">
				<label for="appName">App Name</label>
				<input type="text" name="appName" id="appName"> 
			</div>	
			<div class="form-group">
				<button type="submit" id="btnSubmit" class="btn btn-primary" onclick="register.Register_Evt()">Sign up</button>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		var register= {
			Load:function(){
				
			},
			Register_Evt:function(){
				console.log("Register_Evt");
				$("#btnSubmit").prop("disabled", true);
				var data = new FormData($('#registerForm')[0]);
				$.ajax({
					type: "POST",
					url: "/registerProc",
					data: data,
					enctype: 'multipart/form-data',
					processData: false, 
			        contentType: false,
			        cache: false,
					success: function(response){
						console.log(response);
						$("#btnSubmit").prop("disabled", false);
					},
					error: function(e){
						console.log(e);
					}
				});
			}
		}
		$(document).ready(function(){
			register.Load();
		});
	</script>
</body>
</html>