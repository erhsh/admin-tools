package com.erhsh.work.admintools.service;

import java.util.List;

import com.erhsh.work.admintools.vo.DeviceVO;

public interface IDeviceService {
	/**
	 * 获取用户
	 * 
	 * @param id
	 *            用户Id
	 * @return UserVO
	 */
	DeviceVO getDevice(String id);

	/**
	 * 模糊查询设备
	 * 
	 * @param target
	 *            设备关键字
	 * @return 设备列表
	 */
	List<DeviceVO> getDevices(String target);

	/**
	 * 重值设备属主
	 * 
	 * @param deviceId
	 */
	void resetDeviceOwner(String deviceId);

	/**
	 * 关闭资源
	 */
	void destroy();
}
