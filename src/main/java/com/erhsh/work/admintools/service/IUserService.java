package com.erhsh.work.admintools.service;

import java.util.List;

import com.erhsh.work.admintools.vo.UserVO;

public interface IUserService {

	/**
	 * 获取用户
	 * 
	 * @param id
	 *            用户Id
	 * @return UserVO
	 */
	UserVO getUser(String id);

	/**
	 * 获取全部用户信息
	 * 
	 * @return List
	 */
	List<UserVO> getUsers(String target);

	/**
	 * 充值用户登录失败信息
	 * 
	 * @param id
	 *            用户Id
	 */
	void resetFaildLoginTimes(String id);

	/**
	 * 清除用户设备关系
	 * 
	 * @param id
	 *            用户Id
	 */
	void cleanUserDeviceRels(String id);

	/**
	 * 邮件去激活
	 * 
	 * @param id
	 *            用户Id
	 */
	void cleanMailRegister(String id);

	/**
	 * 删除用户
	 * 
	 * @param id
	 *            用户Id
	 */
	void delUser(String id);

	/**
	 * 关闭资源
	 */
	void destroy();
}
