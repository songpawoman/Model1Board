<%@page import="org.sp.model1board.domain.Gallery"%>
<%@page import="java.util.List"%>
<%@page import="org.sp.model1board.model.GalleryDAO"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%!
	GalleryDAO galleryDAO=new GalleryDAO();
%>
<%
	List<Gallery> galleryList = galleryDAO.selectAll();

	int totalRecord=galleryList.size();//총 레코드 수	
	int pageSize=10;//한 페이지당 보여질 레코드 수
	int totalPage=(int)Math.ceil((float)totalRecord/pageSize); //총 페이지 수
	int blockSize=10; //블럭당 보여질 페이지 수
	int currentPage=1; //현재 보고 있는 페이지 
	
	//만일 파라미터가 넘어오는 경우라면. 즉, 페이지 링크를 통해 들어오면...
	if(request.getParameter("currentPage") !=null){
		currentPage =Integer.parseInt(request.getParameter("currentPage"));
	}
	
	int firstPage=currentPage - ((currentPage-1)%blockSize); //블럭당 반복문의 시작 페이지 
	int lastPage=firstPage+(blockSize-1); //블럭당 반복문의 마지막 페이지 
	int curPos=(currentPage-1)*pageSize; //페이지당 보여질 List의 index
	int num=totalRecord - curPos; //페이지당 시작 번호
%>
<%//="totalRecord = "+totalRecord +"<br>"%>
<%//="pageSize = "+pageSize  +"<br>"%>
<%//="totalPage = "+totalPage  +"<br>"%>
<%//="blockSize = "+blockSize  +"<br>"%>
<%//="currentPage = "+currentPage  +"<br>"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
table {
	border-collapse: collapse;
	border-spacing: 0;
	width: 100%;
	border: 1px solid #ddd;
}

th, td {
	text-align: left;
	padding: 16px;
}

tr:nth-child(even) {
	background-color: #f2f2f2;
}

a{
	text-decoration:none;
}

/*선택된 페이지에 효과 정의*/
.numStyle{
	font-weight:bold;
	font-size:28px;
	color:red;
}
</style>
</head>
<body>


	<table>
		<tr>
			<th>No</th>
			<th>이미지</th>
			<th>제목</th>
			<th>작성자</th>
			<th>등록일</th>
			<th>조회수</th>
			<th>다운로드</th>
		</tr>
		<%for(int i=1;i<=pageSize;i++){%>		
		<%if(num<1)break; %>
		<%Gallery gallery=galleryList.get(curPos++);%>		
		<tr>
			<td><%=num--%></td>
			<td><img src="/data/<%=gallery.getFilename()%>" width="35px"></td>
			<td><a href="/gallery/content.jsp?gallery_idx=<%=gallery.getGallery_idx()%>"><%=gallery.getTitle() %></a></td>
			<td><%=gallery.getWriter() %></td>
			<td><%=gallery.getRegdate() %></td>
			<td><%=gallery.getHit() %></td>
			<td><a href="/gallery/download?filename=<%=gallery.getFilename() %>"><%=gallery.getFilename() %></a></td>
		</tr>
		<%}%>
		<tr>
			<td colspan="7" align="center">
				<%if(firstPage-1 > 0){%>
					<a href="/gallery/list.jsp?currentPage=<%=firstPage-1%>">◀</a>
				<%}else{%>
					<a href="javascript:alert('처음 페이지입니다');">◀</a>
				<%} %>
				
				<%for(int i=firstPage;i<=lastPage;i++){ %>
					<%if(i>totalPage)break; %>					
					<a  <%if(currentPage==i){%>class="numStyle"<%}%>   href="/gallery/list.jsp?currentPage=<%=i%>">[<%=i%>]</a>
				<%} %>
				<%if(lastPage+1> totalPage){%>
					<a href="javascript:alert('마지막 페이지입니다');">▶</a>
				<%}else{ %>
					<a href="/gallery/list.jsp?currentPage=<%=lastPage+1%>">▶</a>
				<%} %>	
			</td>
		</tr>
		
		<tr>
			<td colspan="7"">
				<button onClick="location.href='/gallery/registform.jsp';">글쓰기</button>
			</td>
		</tr>

	</table>

</body>
</html>
