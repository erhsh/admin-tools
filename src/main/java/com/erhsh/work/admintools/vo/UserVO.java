package com.erhsh.work.admintools.vo;

import java.util.List;

public class UserVO {
	private String id;
	private String loginName;
	private String nick;
	private String email;
	private String phone;
	private String failedLoginTimes;
	private String emailAvailable;

	private FamilyVO family;

	private List<DeviceVO> devices;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFailedLoginTimes() {
		return failedLoginTimes;
	}

	public void setFailedLoginTimes(String failedLoginTimes) {
		this.failedLoginTimes = failedLoginTimes;
	}

	public FamilyVO getFamily() {
		return family;
	}

	public void setFamily(FamilyVO family) {
		this.family = family;
	}

	public List<DeviceVO> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceVO> devices) {
		this.devices = devices;
	}

	public String getEmailAvailable() {
		return emailAvailable;
	}

	public void setEmailAvailable(String emailAvailable) {
		this.emailAvailable = emailAvailable;
	}

	public boolean isEmpty() {
		return (null == this.id) || (null == this.loginName);
	}

	@Override
	public String toString() {
		return "UserVO [id=" + id + ", loginName=" + loginName + ", nick="
				+ nick + ", email=" + email + "]";
	}

}
