package com.erhsh.work.admintools.vo;

public class FamilyVO {
	private String id;

	private UserVO creator;

	public FamilyVO() {
	}

	public FamilyVO(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserVO getCreator() {
		return creator;
	}

	public void setCreator(UserVO creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "FamilyVO [id=" + id + "]";
	}

}
