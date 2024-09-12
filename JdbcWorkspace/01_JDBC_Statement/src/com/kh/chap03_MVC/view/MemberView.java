package com.kh.chap03_MVC.view;

import java.util.List;
import java.util.Scanner;

import com.kh.chap03_MVC.controller.MemberController;
import com.kh.model.vo.Member;

/*
 *  View : 사용자가 보게될 시각적인 요소를 담당. (화면 => 입력, 출력)
 * */
public class MemberView {

	private Scanner sc = new Scanner(System.in);

	private MemberController mc = new MemberController();

	/**
	 * 사용자가 보게될 메인화면
	 */
	public void mainMenu() {

		while (true) {

			System.out.println("***** 회원 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름 키워드 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴 기능");
			System.out.println("9. 프로그램 종료");
			System.out.println("---------------------------------");
			System.out.print("이용할 메뉴 선택 : ");
			int menu = Integer.parseInt(sc.nextLine());

			switch (menu) {
			case 1:
				insertMember();
				break;
			case 2:
				selectAll();
				break;
			case 3:
				selectByUserId();
				break;
			case 4:
				selectByUserName();
				break;
			case 5:
				updateMember();
				break;
			case 6:
				deleteMember();
				break;
			case 9:
				System.out.println("프로그램을 종료합니다..");
				break;
			default:
				System.out.println("잘못된 메뉴를 선택했습니다. 다시입력해주세요.");
			}
		}

	}

	private void deleteMember() {
		System.out.println("---- 회원탈퇴 ----");
		
		System.out.print("탈퇴할 회원의 ID : ");
		String userId = sc.nextLine();

		System.out.print("탈퇴할 회원의 PWD : ");
		String userPwd = sc.nextLine();

		mc.deleteMember(userId, userPwd);
		// delete가 아닌 update로 회원탈퇴처리.
		// 성공시 displaySuccess("회원탈퇴 성공")
		// 실패시 displayFail("회원 탈퇴 실패")
	}

	private void updateMember() {
		System.out.println("---- 회원정보 변경 ----");

		// 변경할 회원의 아이디
		System.out.print("변경할 회원의 아이디 : ");
		String userId = sc.nextLine();

		System.out.print("변경할 회원의 비밀번호 : ");
		String userPwd = sc.nextLine();

		// 변경할 수 있는 정보들
		// 이메일, 핸드폰 번호, 주소
		System.out.print("변경할 이메일 : ");
		String email = sc.nextLine();

		System.out.print("변경할 폰번호 : ");
		String phone = sc.nextLine();

		System.out.print("변경할 주소 : ");
		String address = sc.nextLine();

		mc.updateMember(userId, userPwd, email, phone, address);

	}

	private void selectByUserName() {
		System.out.println("--- 회원 이름 키워드 검색 ---");

		System.out.print("회원 이름 키워드 입력 : ");
		String keyword = sc.nextLine();

		mc.selectByUserName(keyword);
		// 조회성공시 displayList호출
		// 조회실패시 displayNoData("키워드로 검색된 결과가 없습니다.")
	}

	/**
	 * 사용자에게 검색할 회원의 아이디를 입력받은 후 조회 요청하는 메소드
	 */
	private void selectByUserId() {
		System.out.println("---- 회원 아이디로 검색 ----");

		System.out.print("검색할 회원의 아이디 : ");
		String userId = sc.nextLine();

		mc.selectUserId(userId);
	}

	/**
	 * 회원 전체 조회 화면
	 */
	private void selectAll() {
		System.out.println("----- 회원 전체 조회 -----");

		// 회원 전체 요청
		mc.selectAll();

	}

	/**
	 * 회원 추가용 View 추가하고자하는 회원의 정보를 입력받아 controller에 회원 추가 요청을 할수 있는 화면
	 */
	private void insertMember() {
		System.out.println("----- 회원추가 -----");

		System.out.print("아이디 : ");
		String userId = sc.nextLine();

		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();

		System.out.print("이름 : ");
		String userName = sc.nextLine();

		System.out.print("성별(M/F) : ");
		char gender = sc.nextLine().toUpperCase().charAt(0);

		System.out.print("이메일 : ");
		String email = sc.nextLine();

		System.out.print("생일 : ");
		String birthday = sc.nextLine();

		System.out.print("핸드폰 : ");
		String phone = sc.nextLine();

		System.out.print("주소 : ");
		String address = sc.nextLine();

		// 입력받은 정보를 컨트롤러에게 넘겨서 회원 추가 요청.
		mc.insertMember(userId, userPwd, userName, gender, email, birthday, phone, address);
	}

	/**
	 * 서비스 요청 성공시 보게될 화면
	 * 
	 * @param string
	 */
	public void displaySuccess(String string) {
		System.out.println("\n서비스 요청 성공 ! " + string);
	}

	/**
	 * 서비스 요청 실패시 보게될 화면
	 * 
	 * @param string
	 */
	public void displayFail(String string) {
		System.out.println("\n서비스 요청 실패.. " + string);
	}

	/**
	 * 조회서비스 요청시 "여러행"이 조회된 결괴를 받아서 보여줄 화면
	 * 
	 * @param list
	 */
	public void displayList(List<Member> list) {
		System.out.println("\n조회된 데이터는 " + list.size() + "건 입니다.\n");

		for (Member m : list) {
			System.out.println(m);
		}
	}

	/*
	 * 조회서비스 요청시 결과가 없을 때 보여줄 화면
	 */
	public void displayNoDate(String string) {
		System.out.println(string);
	}

	public void displayOne(Member m) {
		System.out.println("----- 조회결과 -----");

		System.out.println("회원의 아이디 : " + m.getUserId());
		System.out.println("회원의 이름 : " + m.getUserName());
		System.out.println("회원의 핸드폰번호 : " + m.getPhone());
		System.out.println("회원의 주소 : " + m.getAddress());
	}

	public void displayNoData(String string) {
		System.out.println(string);

	}

	public void displayList(String keyword) {
		System.out.println(keyword);

	}

}