package com.erhsh.work.admintools.vo;

public class DeviceVO {
	private String id;
	private String mac;
	private String mac2;
	private String name;
	private String dv;
	private String sn;
	private String addr;
	private String createStamps;

	private DeviceVO parent;
	private UserVO owner;

	public DeviceVO(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMac2() {
		return mac2;
	}

	public void setMac2(String mac2) {
		this.mac2 = mac2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCreateStamps() {
		return createStamps;
	}

	public void setCreateStamps(String createStamps) {
		this.createStamps = createStamps;
	}

	public DeviceVO getParent() {
		return parent;
	}

	public void setParent(DeviceVO parent) {
		this.parent = parent;
	}

	public UserVO getOwner() {
		return owner;
	}

	public void setOwner(UserVO owner) {
		this.owner = owner;
	}

	public boolean isEmpty() {
		return null == this.id || null == this.mac || "".equals(this.mac);
	}

	@Override
	public String toString() {
		return "DeviceVO [id=" + id + ", mac=" + mac + ", mac2=" + mac2
				+ ", name=" + name + "]";
	}

}
