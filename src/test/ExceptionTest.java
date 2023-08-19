package test;

import java.sql.PreparedStatement;

public class ExceptionTest {

	//testO() 메서드 호출부에게 예외 처리를 전가한다는 의미...
	public void test() throws NumberFormatException{
		String msg="나";
		
		
		//개발자에게try~catch 처리를 강요하지 않는 예외를 가리켜 RuntimeExcetion 이라 하며 
		//RuntimeException 을 처리할지 여부는 오직 개발자 몫이다..
		int x=Integer.parseInt(msg);
		System.out.println(x); 
		/*
		try {
		}catch(NumberFormatException e) {
			System.out.println("정수화 될수 없네요 . 알맞는 숫자형 문자를 사용하세요");
		}
		*/
	}
	
	public static void main(String[] args){
		ExceptionTest et = new ExceptionTest();
		try {
			et.test();
		}catch(NumberFormatException e) {
			System.out.println("정수화 될수 없네요 . 알맞는 숫자형 문자를 사용하세요");
		}
		
	}
}
