package com.kh.model.vo;

import java.sql.Date;

/*
 *  VO(Value Object)
 *  - 어플리케이션을 이루는데 있어서 "핵심적인" 역할을 하는 클래스를 정의하는 클래스.
 *  - 필요에 의해 hashCode, equals, 그외 함수를 정의할 수 있음.
 *  - DB테이블의 정보를 기록하는 용도의 객체
 *  
 *  DTO(Data Transfer Object)
 *  - 데이터 전송용 객체
 *  - API서버간의 데이터 송수신시에만 사용된다.
 *  
 * 
 * */
public class Member {
   private int userNo;      //	USER_NO	NUMBER
   private String userId;   //	USER_ID	VARCHAR2(30 BYTE)
   private String userPwd;  //	USER_PWD	VARCHAR2(100 BYTE)
   private  String userName; // USER_NAME	VARCHAR2(15 BYTE)
   private String Email;    //	EMAIL	VARCHAR2(100 BYTE)
   private  String Birthday; //	BIRTHDAY	VARCHAR2(6 BYTE)
   private char Gender;     // GENDER	VARCHAR2(1 BYTE)
   private String Phone;    //	PHONE	VARCHAR2(13 BYTE)
   private String Address;  //	ADDRESS	VARCHAR2(100 BYTE)
   private Date EnrollDate;     //	ENROLL_DATE	DATE
   private Date ModifyDate;     //	MODIFY_DATE	DATE
   private char Status;      //	STATUS	VARCHAR2(1 BYTE)

   public Member() {
	   
   }

public int getUserNo() {
	return userNo;
}

public void setUserNo(int userNo) {
	this.userNo = userNo;
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public String getUserPwd() {
	return userPwd;
}

public void setUserPwd(String userPwd) {
	this.userPwd = userPwd;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getEmail() {
	return Email;
}

public void setEmail(String email) {
	Email = email;
}

public String getBirthday() {
	return Birthday;
}

public void setBirthday(String birthday) {
	Birthday = birthday;
}

public char getGender() {
	return Gender;
}

public void setGender(char gender) {
	Gender = gender;
}

public String getPhone() {
	return Phone;
}

public void setPhone(String phone) {
	Phone = phone;
}

public String getAddress() {
	return Address;
}

public void setAddress(String address) {
	Address = address;
}

public Date getEnrollDate() {
	return EnrollDate;
}

public void setEnrollDate(Date enrollDate) {
	EnrollDate = enrollDate;
}

public Date getModifyDate() {
	return ModifyDate;
}

public void setModifyDate(Date modifyDate) {
	ModifyDate = modifyDate;
}

public char getStatus() {
	return Status;
}

public void setStatus(char status) {
	Status = status;
}

@Override
public String toString() {
	return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
			+ ", Email=" + Email + ", Birthday=" + Birthday + ", Gender=" + Gender + ", Phone=" + Phone + ", Address="
			+ Address + ", EnrollDate=" + EnrollDate + ", ModifyDate=" + ModifyDate + ", Status=" + Status + "]";
}



}