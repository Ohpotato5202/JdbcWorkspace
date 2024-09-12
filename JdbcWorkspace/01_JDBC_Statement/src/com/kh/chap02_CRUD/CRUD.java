package com.kh.chap02_CRUD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.kh.model.vo.Member;

public class CRUD {
	
	public static void main(String[] args) {
		CRUD crud = new CRUD();
		
		Scanner sc = new Scanner(System.in);
//		System.out.print("이메일 : ");
//		String email = sc.nextLine();
//		
//		System.out.print("핸드폰 : ");
//		String phone = sc.nextLine();
//		
//		System.out.print("주소 : ");
//		String address = sc.nextLine();
		
//		System.out.print("아이디 : ");
//		String userId = sc.nextLine();
		
		
		
		//crud.updateMember(email, phone, address, userId);
		//crud.deleteMember(userId);
		//crud.selectOne(userId);
		//crud.selectAll();
		//crud.execPlSql();
		
		System.out.print("사번 : ");
		int empId = sc.nextInt();
		
		crud.exeProcedure(empId);
	}
	
	

	/**
	 * 회원가입 메서드
	 * DML(INSERT, UPDATE, DELETE) JDBC 코딩 흐름 <br>
	 * 1) 드라이버(생략가능) - 자동등록됨 <br>
	 * 2) DBMS연결 == Connection객체 생성 <br>
	 * 3) autoCommit 설정 변경 : true(기본값) / false <br>
	 * 4) Statement객체 생성 <br>
	 * 5) SQL문 실행 -> execute(SQL) + getUpdateCount() || executeUpdate(SQL) <br>
	 * 6) 트랜잭션 처리 <br>
	 * 7) 자원반납 <br>
	 */
	public void insertMember() {
		// 2)
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe"
					, "C##JDBC", "JDBC");
			// 3) AutoCommit 설정
			conn.setAutoCommit(false);
			
			// 4) Statement객체생성
			Statement stmt = conn.createStatement();
			
			// 5+6) DB에 완성된 SQL문 전달하면서 실행후 처리결과값(처리된 행의 갯수)돌려받기
			// Statement ? 실행할 SQL문장을 완전한 형태로 만들어서 실행해야 하는 클래스
			String sql = "INSERT INTO MEMBER VALUES(SEQ_UNO.NEXTVAL, 'user02', 'pass02', '홍길동', 'user02@naver.com'"
					+ ", '900213', 'M', '010-1234-1234', '서울시 마포구 공덕동', SYSDATE, SYSDATE, DEFAULT)";

			int result = stmt.executeUpdate(sql);
			
			System.out.println("처리 결과 : "+result);
			
			// 7) 트랜잭션처리
			if(result > 0) { // 성공시
				conn.commit();
			}else {
				conn.rollback();
			}
			
			// 8) 다쓴 자원 반납
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * 4) PreparedStatement 객체 생성  
	 * */
	public void updateMember(String email,String phone, String address, String userId) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC","JDBC");
			conn.setAutoCommit(false);
			
			// 4_1) PreparedStatement객체 생성
			/*    PreparedStatement ?
			 *    - Prepared -> 준비된, 준비된 Statement, SQL문을 미리 준비해둔 클래스
			 *    - Statement 인터페이스를 확장한 인터페이스, 즉 더 많은 기능이 있으며 Statement단점을 개선한 인터페이스
			 *    - Statement의 단점?
			 *      1. 하드코딩한 데이터가 그대로 들어가 있어서 가독성이 안좋음.
			 *      2. 재사용성이 좋지 못함.
			 *      3. SQL인젝션을 방어할 수 없음.
			 *      - 객체생성시 "미완성된" 상태의 SQL문을 미리 전달하여 실행계획을 "준비시키고", 실행하기 전에 완성된 형태로 가공한후 실행한다. 
			 * */
			
			sql = "UPDATE MEMBER SET EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			
			// 4_2) 미완성된 쿼리문 완성된 형태로 변환하기.
			// pstmt.setXXX(?의 위치, 넣어줄값); 
			pstmt.setString(1, email);
			pstmt.setString(2, phone);
			pstmt.setString(3, address);
			pstmt.setString(4, userId);
			
			// 5_6) executeUpdate
			int result = pstmt.executeUpdate();
			
			if (result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void deleteMember(String userId) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		String sql = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe" , "C##JDBC", "JDBC");
			conn.setAutoCommit(false);
			
			// SQL Injetion알아보기
			// 1) Statement로 객체생성
			stmt = conn.createStatement();
			
			// 쿼리문 실행
			//int result = stmt.executeUpdate("DELETE FROM MEMBER WHERE USER_ID = '"+userId+"'");
			sql = "DELETE FROM MEMBER WHERE USER_ID = ?";
			
			// 2) PreparedStatement로 객체 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			int result = pstmt.executeUpdate(); // executeUpdate() 함수 실행시 쿼리문은 다시 미완성된 상태로 돌아간다.
			System.out.println("처리된 행의 갯수 : " +result);
			
			// 미완성된 쿼리문 재사용.
			pstmt.setString(1, userId);
			int result2 = pstmt.executeUpdate(); 
			System.out.println("처리된 행의 갯수 : " +result);
			
			int total = result * result2;
			
			if(total > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 회원 한명 조회하는 메소드 <br>
	 * 
	 * DQL(SELECT)시 JDBC의 코딩 흐름
	 * 1) Driver등록(생략가능)
	 * 2) DBMS연결
	 * 3) PreparedStratement객체 생성
	 * 4) SQL실행
	 * 5) 결과값 반환
	 * 6) ResultSet객체를 알맞은 vo클래스로 매핑
	 * 7) 다쓴 자원 반납
	 * */
	public void selectOne(String userId) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member m = null;
		String sql = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe" , "C##JDBC", "JDBC");
			
			sql = "SELECT * FROM MEMBER WHERE USER_ID = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			/*
			 * ResultSet?
			 *  - SELETE를 호출한 결과값이 담겨있는 객체
			 *  - 커서(Cursor)를 사용하여 ResultSet의 각 "행" 에 접근하여 데이터를 읽어올 수 있음.
			 *  * Cusor? ResultSet내부에서 특정 "행"을 가리키는 포인터
			 *  - 커서의 위치를 변경하는 메서드들
			 *   1) next() : boolean -> 커서의 위치를 "다음" 행으로 이동시키고 이동시킨 자리에 행이 있으면 true, 없으면 false
			 *   2) previous() : boolean -> 커서의 위치를 "이전" 행으로 이동시키고 이동시킨 자리에 행이 있으면 true, 없으면 false
			 *   3) first() : boolean
			 *   4) last() : boolean
			 *   5) absolute(int row) : boolean -> 커서를 지정된 행으로 이동시키고 행이 있다면 true, 없다면 false.
			 *   6) relative(int rows) : boolean -> 커서를 "현재" 위치에서 지정된 수만큼 이동시키고 행이 있다면 true, 없다면 false
			 *   
			 * */
			// 6) rset를 vo객페로 매핑
			if(rset.next());
			
			// 데이터가 존재한다면 현재 행의 데이터를 뽑아서 Member객체로 변환.

			// rset으로부터 어떤 컬럼이 해당하는 값을 뽑을건지 제시
			// 컬럼명, 컬럼의 순번
			// 권장사항은 컬럼명(대문자)쓰는 것을 권장.
			
			m = new Member();
			m.setUserNo(rset.getInt("USER_NO"));
			m.setUserId(rset.getString("USER_ID"));
			m.setUserPwd(rset.getString("USER_PWD"));
			m.setUserName(rset.getString("USER_NAME"));
			m.setGender(rset.getString("GENDER").charAt(0));
			m.setEmail(rset.getString("EMAIL"));
			m.setPhone(rset.getString("PHONE"));
			m.setAddress(rset.getString("ADDRESS"));
			m.setEnrollDate(rset.getDate("ENROLL_DATE"));
			m.setModifyDate(rset.getDate("MODIFY_DATE"));
			m.setStatus(rset.getString("STATUS").charAt(0));
			
			System.out.println(m);
	       } catch (SQLException e) {
	         e.printStackTrace();
	      }finally {
	         try {
	            rset.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	         
	         try {
	            pstmt.close();
	         } catch (SQLException e) {            
	            e.printStackTrace();
	         }
	         
	         try {
	            conn.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	      }
	}
	
	// 한번에 여러행 데이터 조회하기
	public void selectAll() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = null;
		List<Member> list = new ArrayList<>();
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##JDBC","JDBC");
			
			sql = "SELECT * FROM MEMBER";
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Member m = new Member();
				m.setUserNo(rset.getInt("USER_NO"));
				m.setUserId(rset.getString("USER_ID"));
				m.setUserPwd(rset.getString("USER_PWD"));
				m.setUserName(rset.getString("USER_NAME"));
				m.setGender(rset.getString("GENDER").charAt(0));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
				m.setModifyDate(rset.getDate("MODIFY_DATE"));
				m.setStatus(rset.getString("STATUS").charAt(0));
				
				list.add(m);
			}
			System.out.println("회원 list의 길이 : "+list.size());
			System.out.println(list);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	         try {
	            rset.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	         
	         try {
	            pstmt.close();
	         } catch (SQLException e) {            
	            e.printStackTrace();
	         }
	         
	         try {
	            conn.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	      }
	}
	
	// JDBC로 PL/SQL문 호출하기.
	   public void execPlSql() {
	      
	      try {
	         Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##JDBC","JDBC");
	         
	         String sql = "BEGIN UPDATE MEMBER SET USER_NAME = ? ; END;";
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, "김민욱");
	         
	         //pl,sql
	         boolean result = pstmt.execute();
	         
	         pstmt.close();
	         conn.close();
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	   }	
	   
	   // JDBC로 내장 프로시져를 실행
	   // (KH계정의 PRO_SELECT_EMP 프로시져 실행)
	   public void exeProcedure(int empId) {
		   
		   try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##KH","KH");
			
			// 3) CallableStatement객체 생성.
			//    CallableStatement ?
			//    - 저장된 프로시저를 호출할때 사용한다.
			String sql = "{CALL PRO_SELECT_EMP(?,?,?,?)}"; // 1번 위치홀더 : IN, 2, 3, 4번 위치홀더 : OUT
			CallableStatement cstmt = conn.prepareCall(sql);
			
			cstmt.setInt(1, empId);
			cstmt.registerOutParameter(2, Types.VARCHAR); // EMP_NAME -> VACHAR2
			cstmt.registerOutParameter(3, Types.INTEGER); // SALARY : NUMBER
			cstmt.registerOutParameter(4, Types.DOUBLE); // BONUS  : NUBER
			
			// 프로시져 실행
			cstmt.execute();

			String name = cstmt.getString(2);
			int salary = cstmt.getInt(3);
			Double bonus = cstmt.getDouble(4);

			System.out.println(name + ", " + salary + ", " + bonus);

			cstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	   
	   
	
}

	      
	      
	   

