package com.kh.chap03_MVC.model.service;

import java.sql.Connection;
import java.util.List;

import com.kh.chap03_MVC.model.dao.MemberDao;
import com.kh.common.template.JDBCTemplate;
import static com.kh.common.template.JDBCTemplate.*;
import com.kh.model.vo.Member;

/*
 *   Service : 컨트롤러 의해 호출되는 최초의 메소드.
 *             여러 dao에 존재하는 메소드를 호출하여 논리적으로 연관이 있는 비즈니스 로직을 만든다.   
 *             처리 결과값을 컨트롤러에게 반환해주는 역할을 한다.
 * 
 * */
public class MemberService {

	private MemberDao mDao = new MemberDao();

	public int insertMember(Member m) {
		// Connection객체 생성
		Connection conn = JDBCTemplate.getConnection();

		// DAO호출시 Connection 객체와 기존에 넘기고자했던 매개변수(m)을 같이 넘겨준다.
		int result = mDao.insertMember(conn, m); // 처리된 행의 갯수 반환

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);

		return result;
	}

	public List<Member> selectAll() {
		Connection conn = getConnection();
		
		List<Member> list = mDao.selectAll(conn);
		
		close(conn);
		
		return list;

	}

	public Member selectUserId(String userId) {		
		Connection conn = getConnection();
		
		Member m = mDao.selectUserId(conn, userId);
		
		close(conn);
		
		return m;
	}

	public List<Member> selectByUserName(String keyword) {
		Connection conn = getConnection();
		
		List<Member> list = mDao.selectByUserName(conn, keyword);
		
		close(conn);
		
		return list;
	}

	public int selectUser(String userId, String userPwd) {
		Connection conn = getConnection();
		
		int result = mDao.selectUser(conn, userId, userPwd);
		
		close(conn);
		
		return result;
	}

	public int updateMember(Member m) {
		Connection conn = getConnection();
		
		int result = mDao.updateMember(conn, m);
		
		if(result > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}

	public int deleteMember(String userId, String userPwd) {
		Connection conn = getConnection();
		
		int result = mDao.deleteMember(conn, userId, userPwd);
		
		if(result > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		close(conn);
		return result;
	}

}