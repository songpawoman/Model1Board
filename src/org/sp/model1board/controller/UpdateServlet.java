package org.sp.model1board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
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

public class UpdateServlet extends HttpServlet {
	GalleryDAO galleryDAO;
	DiskFileItemFactory factory;
	int maxSize=1024*1024*2;
	String path;
	
	Gallery gallery;
	String encoding="utf-8";
	
	public void init(ServletConfig config) throws ServletException {
		galleryDAO = new GalleryDAO();
		factory = new DiskFileItemFactory();
		path = config.getServletContext().getRealPath("/data");
		File file = new File(path);
		
		factory.setSizeThreshold(maxSize); //용량 제한
		factory.setRepository(file); //저장위치
		
		System.err.println(path);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		ServletFileUpload upload=new ServletFileUpload(factory);
		
		try {
			List<FileItem> itemList = upload.parseRequest(request);
			gallery = new Gallery();
			
			for(FileItem item: itemList) {
				if(item.isFormField()) {
					
					String data=item.getString(encoding);
					
					switch(item.getFieldName()) {
						case "gallery_idx":gallery.setGallery_idx(Integer.parseInt(data));break;
						case "original":gallery.setFilename(data);break; //일단 유지로 처리한다
						case "title":gallery.setTitle(data);break; 
						case "writer":gallery.setWriter(data);break;
						case "content":gallery.setContent(data);break;
					}
				}else {
					System.out.println("넘어온 파일의 사이즈는  "+item.getSize());
					System.out.println("넘어온 파일명 길이는  "+item.getString().length());
					System.out.println("넘어온 파일의 contentType은 "+item.getContentType());
					
					//새롭게 업로드 된게 있다면.
					if(item.getSize()>0 && item.getString().length()>0) {
						String uploadName=item.getName();
						long time=System.currentTimeMillis();
						String ext = uploadName.substring(uploadName.lastIndexOf(".")+1,  uploadName.length());
						String filename=time+"."+ext;
						
						File file =new File(path, filename);
						
						try {
							 //새로운 파일 저장
							item.write(file);
							
							//기존 파일 삭제 
							File delFile = new File(path, gallery.getFilename());
							delFile.delete();
							
							//새로운 파일명을 DTO에 대입
							gallery.setFilename(filename);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}//for end
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		  
		  int result = galleryDAO.update(gallery);
		  
		  PrintWriter out=response.getWriter();
		  out.print("<script>");
		  if(result>0) {
			  out.print("alert('수정완료');");
			  out.print("location.href='/gallery/content.jsp?gallery_idx="+gallery.getGallery_idx()+"';");
		  }else {
			  out.print("alert('수정실패');");
			  out.print("history.back();");
		  }
		  out.print("</script>");		 		
	}
}
