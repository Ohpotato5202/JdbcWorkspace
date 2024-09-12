package com.kh.chap03_MVC.model.dao;

import static com.kh.common.template.JDBCTemplate.close;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.kh.common.template.JDBCTemplate;
import com.kh.model.vo.Member;

/*
 *    DAO(Data Access Object)?
 *    - Service에 의해 호출되며, 맡은 기능을 수행하기 위해 db에 직접 접근하여 sql문을 호출한 후 
 *      처리 결과값을 반환시켜주는 객체 
 * */
public class MemberDao {
    
	private Properties prop = new Properties();
	
	public MemberDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/member_mapper.xml"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 사용자가 view에서 입력한 값을 가지고 db에 INSERT문을 실행하는 메소드
	 * 
	 * @param conn
	 * @param m
	 * @return 처리된 행의 갯수
	 */
	public int insertMember(Connection conn, Member m) {

		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getEmail());
			pstmt.setString(5, m.getBirthday());
			pstmt.setString(6, String.valueOf(m.getGender()));
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	/**
	 * 사용자가 회워 전체 요청시 select문을 통해 전체 회원을 조회하는 메서드
	 * 
	 * @param conn
	 * @return
	 */
	public List<Member> selectAll(Connection conn) {

		List<Member> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "SELECT * FROM MEMBER WHERE STATUS = 'Y'";
		Member m = null;

		try {
			pstmt = conn.prepareStatement(sql);

			rset = pstmt.executeQuery();

			while (rset.next()) {

				// 현재 rset의 커서가 가리키는 지점에서 데이터 추출
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

				list.add(m);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
			// close(rset);
			// close(pstmt);
		}
		return list;
	}

	public Member selectUserId(Connection conn, String userId) {
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "SELECT * FROM MEMBER WHERE USER_ID = ? AND STATUS = 'Y'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);

			rset = pstmt.executeQuery();

			if (rset.next()) {

				m = new Member();
				m.setUserId(rset.getString("USER_ID"));
				m.setUserName(rset.getString("USER_NAME"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
		}

		return m;
	}

	public List<Member> selectByUserName(Connection conn, String keyword) {
		List<Member> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "SELECT * FROM MEMBER WHERE USER_NAME LIKE ? AND STATUS = 'Y'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");

			rset = pstmt.executeQuery();

			while (rset.next()) {

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

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
		}

		return list;
	}

	public int selectUser(Connection conn, String userId, String userPwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "SELECT COUNT(*) FROM MEMBER WHERE USER_ID = ? AND USER_PWD = ? AND STATUS = 'Y'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				result = rset.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
		}

		return result;
	}

	public int updateMember(Connection conn, Member m) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "UPDATE MEMBER SET "
				+ "EMAIL = ?,\r\n"
				+ "PHONE = ?,\r\n"
				+ "ADDRESS = ?,\r\n"
				+ "MODIFY_DATE = SYSDATE\r\n"
				+ "WHERE USER_ID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getEmail());
			pstmt.setString(2, m.getPhone());
			pstmt.setString(3, m.getAddress());
			pstmt.setString(4, m.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}		
		return result;
	}
	
	public int deleteMember(Connection conn, String userId, String userPwd) {
	      
	      PreparedStatement pstmt = null;
	      String sql = "UPDATE MEMBER SET MODIFY_DATE = SYSDATE, STATUS = 'N' WHERE USER_ID = ? AND USER_PWD = ? AND STATUS = 'Y'";
	      int result = 0;
	      
	      try {
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, userId);
	         pstmt.setString(2, userPwd);
	         
	         result = pstmt.executeUpdate();
	         
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }finally {
	         close(pstmt);
	      }
	      return result;
	   }
	
	
	
	
	
	
	
	
	
	
	
}