<%@page import="org.sp.model1board.domain.Gallery"%>
<%@page import="org.sp.model1board.model.GalleryDAO"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%!
	GalleryDAO galleryDAO = new GalleryDAO();
%>
<%
	int gallery_idx = Integer.parseInt(request.getParameter("gallery_idx"));
	Gallery gallery=galleryDAO.select(gallery_idx);
%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {
	font-family: Arial, Helvetica, sans-serif;
}

* {
	box-sizing: border-box;
}

input[type=text], input[type=file], select, textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
	margin-top: 6px;
	margin-bottom: 16px;
	resize: vertical;
}

input[type=submit] {
	background-color: #04AA6D;
	color: white;
	padding: 12px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

input[type=submit]:hover {
	background-color: #45a049;
}

.container {
	border-radius: 5px;
	background-color: #f2f2f2;
	padding: 20px;
}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script type="text/javascript">
function edit(){
	$("form").attr({
		action:"/gallery/update",
		method:"post",
		enctype:"multipart/form-data"
	});	
	$("form").submit();
}

function del(){
	location.href="/gallery/del?gallery_idx=<%=gallery.getGallery_idx()%>";
}

</script>
</head>
<body>

	<div class="container">
		<form>
			<input type="hidden" name="gallery_idx" value="<%=gallery.getGallery_idx()%>">
			<input type="hidden" name="original" value="<%=gallery.getFilename()%>">
			
			<input type="text" name="title" value="<%=gallery.getTitle()%>"> 
			<input type="text" name="writer" value="<%=gallery.getWriter()%>">
			<textarea id="content" name="content" placeholder="Write something.." style="height: 200px"><%=gallery.getContent()%></textarea>
			<div><img src="/data/<%=gallery.getFilename()%>" width="150px"></div>
			<input type="file" name="filename">
			<input type="button" value="목록" onClick="location.href='/gallery/list.jsp';">
			<input type="button" value="수정" onClick="edit()">
			<input type="button" value="삭제" onClick="del()">
		</form>
	</div>

</body>
</html>
