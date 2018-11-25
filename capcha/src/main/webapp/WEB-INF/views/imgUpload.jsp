<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>admin - ImgUpload</title>
<%@ include file="bootstrap.jsp" %>
</head>
<body>
	<div class="container">
		<form method="post" enctype="multipart/form-data" id="uploadForm">
			<div class="form-group">
				<label for="dropdownMenu1">Category</label>
				<div class="dropdown" id="mydropdown">
					<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" name="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
						Select <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
						<c:forEach var="c" items="${categoryList}">
						 	<li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:void(0)" data-value="${c.cno}">${c.name}</a></li>
				          </c:forEach>
					</ul>
				</div>
			</div>
			<input type="hidden" name="category" id="inputCategory">
			<div class="form-group">
				<label for="file">File</label>
				<input type="file" name="file">
			</div>
			<div class="form-group">
			<button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary" onclick=imgUpload.ImgUpload_Evt()>Upload</button>
			</div>
		</form>	
		<script>
			$(".dropdown-menu li a").click(function(){
			  $(this).parents(".dropdown").find('.btn').html($(this).text() + ' <span class="caret"></span>');
			  $(this).parents(".dropdown").find('.btn').val($(this).data('value'));
			  $('#inputCategory').val($(this).data('value'));
			});
		</script>
		<script type="text/javascript">
			var imgUpload = {
				Load: function(){
					
				},
				ImgUpload_Evt: function(){
					var file = new FormData($('#uploadForm')[0]);
					$("#btnSubmit").prop("disabled", true);
					console.log("Send_Evt");
					$.ajax({
						type: "POST",
						url: "/imgUploadProc",
						data: file, 
						enctype: 'multipart/form-data',
						processData: false, 
				        contentType: false,
				        cache: false,
						success: function(response){
							console.log(response);
							alert("Success!");
							$("#btnSubmit").prop("disabled", false);
						},
						error: function(e){
							console.log(e);							
						}
					});
				},
				CheckVaild: function(){
					
				}
			}
			
			$(document).ready(function() {
				imgUpload.Load();
				
			});
		</script>
	</div>
</body>
</html>