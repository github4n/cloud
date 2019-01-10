package com.iot.robot.vo.user;

import com.iot.robot.vo.AuthorVo;




public class LoginUserVo extends AuthorVo {
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**  用户名，可以是邮箱或手机 */
	public String userName = "";
	
	/** 密码 */
	private String passWord = "";
}
