package org.sp.model1board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//클라이언트의 파일 요청을 처리하는 서블릿 
public class DownServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String filename=request.getParameter("filename");
		
		//클라이언트가 요청한 파일의 정보 얻기
		ServletContext context=request.getServletContext();
		String path=context.getRealPath("/data");
		
		File file=new File(path , filename);
		
		//웹브라우저가 문자 인코딩을 사용하지 않게 하려면 , 응답 정보로 보내려는 데이터가 이진 데이터라는
		//것을 알려줘야 한다. 
		//아래와 같이 지정하면, 클라이언트인  브라우저는 이진 데이터를 해석하게 된드.. 
		//주로, 이미지, 음악, 비디오 같은 이진 데이터를 포함하는 응답 정보의 헤더를 구성할때는 아래와 같이
		//컨텐트 타입을 명시하면 된다..
		response.setContentType("application/octet-stream");
		//파일의 길이..
		response.setContentLength((int)file.length());
		
		//파일의 종류: MIME 타입 
		//Content-Disposition: 마임타입; filename=파일명
		response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
		
		//응답 바디에 파일의 내용을 채워넣기 
		//서버의 파일의 데이터를 읽어들여, 응답 정보가 가진 출력스트림에 채워넣기(출력)
		
		byte[] buff = new byte[1024];
		
		FileInputStream fis = new FileInputStream(file);
		OutputStream fos=response.getOutputStream();
		
		int flag=-1; //데이터를 모두 읽어들였는지 체크하기 위한 변수 
		
		while(true) {
			flag=fis.read(buff);//1024개씩 읽어들여(입력)
			if(flag==-1)break;
			fos.write(buff);//1024개씩 내뱉기..(출력)
		}
		fos.close();
		fis.close();
	}
}





