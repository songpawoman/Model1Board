package org.sp.model1board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sp.model1board.domain.Gallery;
import org.sp.model1board.model.GalleryDAO;

public class RegistGallery extends HttpServlet{
	
	DiskFileItemFactory factory; //업로드와 관련된 설정을 담당 
	int maxSize=1024*1024*2;//2m
	String path; 
	String filename; //새롭게 생성된 파일명
	GalleryDAO galleryDAO;
	Gallery gallery;
	String encoding="utf-8";
	
	public void init(ServletConfig config) throws ServletException {
		galleryDAO = new GalleryDAO();
		
		factory = new DiskFileItemFactory();
		factory.setSizeThreshold(maxSize);//용량 제한
		
		//저장 경로를 File 로 표현..
		//application은 jsp의 내장객체이므로, 서블릿으로 개발할때는 사용할 수 없기 때문에
		//application 내장 객체의 자료형을 직접 다룰 줄 알아야 한다..
		//ServletContext 자료형이며, javaEE api
		//ServletContext 를 얻어오는 방법 
		//1) request 객체를 통해 얻는 방법
		//2) ServletConfig 객체를 통해 얻는 방법
		ServletContext context=config.getServletContext();
		path=context.getRealPath("/data");
		System.out.println(path);
		
		File file =new File(path);
		factory.setRepository(file);//서버의 어디에 저장할지...
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//클라이언트에게 전송할 응답정보 
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();

		//클라이언트가 전송한 파라미터들 받기
		//만일 복합된(multipart) 형식인 경우 기존 request만으로는 처리 불가 
		//컴포넌트 이용한다 
		
		//팩토리를  이용하여 설정이 끝났다면, SerlvetFileUpload 클래스를 이용하여 업로드를 실행
		ServletFileUpload upload=new ServletFileUpload(factory);
		boolean flag=false; //업로드 성공 여부를 판단할 수 있는 논리값 
		
		try {
			List<FileItem> itemList=upload.parseRequest(request); //요청분석 
			
			gallery=new Gallery();
			
			//improved loop 개선된 반복문 
			for(FileItem item : itemList) {
				if(item.isFormField()) { //input type="text" name="title" 형태의 파라미터..
					if(item.getFieldName().equals("title")) {
						gallery.setTitle(item.getString(encoding));
					}else if(item.getFieldName().equals("writer")) {
						gallery.setWriter(item.getString(encoding));
					}else if(item.getFieldName().equals("content")) {
						gallery.setContent(item.getString(encoding));
					}
				}else { //input type="file" 형태
					//지정한 경로에 파일저장하기
					long time=System.currentTimeMillis(); //4578394254739.jpg
					
					//String ext=전송된파일의정보에서가져오기;
					System.out.println(item.getName());
					String ext=item.getName().substring(item.getName().lastIndexOf(".")+1 , item.getName().length());
					filename=time+"."+ext; //u4944u12323i14u12.jpg
					
					gallery.setFilename(filename); //DTO에 파일명 담기
					
					File uploadFile=new File(path, filename);
					
					item.write(uploadFile);//디스크에 내려쓰기
					System.out.println("서버에 저장된 파일은 "+uploadFile);
				}
			}	
			flag=true;
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//메시지 출력 후 list를 다시 접속하게
		out.print("<script>");
		if(flag) {//파일 저장이 성공되었다면...db insert
			int result=galleryDAO.insert(gallery);
			
			if(result>0) {
				out.print("alert('등록 성공');");
				out.print("location.href='/gallery/list.jsp';"); //재접속을 유도..
			}else {
				out.print("alert('등록 실패');");
				out.print("history.back();"); //재접속을 유도..
			}
		}else {
			out.print("alert('파일이 저장되지 않았습니다.');");
			out.print("history.back();");
		}
		out.print("</script>");
		
	}
}










