package com.erhsh.work.admintools.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.erhsh.work.admintools.service.IDeviceService;
import com.erhsh.work.admintools.service.IUserService;
import com.erhsh.work.admintools.utils.JedisHelper;
import com.erhsh.work.admintools.vo.DeviceVO;
import com.erhsh.work.admintools.vo.FamilyVO;
import com.erhsh.work.admintools.vo.UserVO;

public class UserServiceImpl implements IUserService {

	private Jedis jedis = JedisHelper.getJedis();

	public UserServiceImpl() {
		System.out.println("UserServiceImpl.UserServiceImpl()");
	}

	@Override
	public UserVO getUser(String id) {
		UserVO result = new UserVO();

		// 用户扩展信息
		String email = jedis.hget("user:email", id); // 邮箱
		String nick = jedis.hget("user:nick", id); // 昵称
		String phone = jedis.hget("user:phone", id); // 电话
		String familyId = jedis.hget("user:family", id); // 所在家庭
		String failedLoginTimes = jedis.get("user:failedLoginTimes:" + id);
		String emailAvailable = jedis.hget("user:emailavailable", id);

		IDeviceService devService = new DeviceServiceImpl();
		List<DeviceVO> devices = new ArrayList<DeviceVO>();
		Set<String> devIds = jedis.smembers("u:" + id + ":devices");
		for (String devId : devIds) {
			DeviceVO device = devService.getDevice(devId);
			devices.add(device);
		}
		devService.destroy();

		result.setId(id);
		result.setLoginName(email);
		result.setEmail(email);
		result.setNick(nick);
		result.setPhone(phone);
		result.setFailedLoginTimes(failedLoginTimes);
		result.setEmailAvailable(emailAvailable);

		if (null != familyId) {
			result.setFamily(new FamilyVO(familyId));
		}
		result.setDevices(devices);

		return result;
	}

	@Override
	public List<UserVO> getUsers(String target) {
		List<UserVO> result = new ArrayList<UserVO>();

		// 正则匹配
		Pattern pattern = Pattern.compile(target);

		// 全部用户列表
		Map<String, String> userMailMap = jedis.hgetAll("user:email");
		for (Entry<String, String> entry : userMailMap.entrySet()) {
			String id = entry.getKey();
			String mail = entry.getValue();

			Matcher matcher = pattern.matcher(mail);
			if (mail.contains(target) || matcher.matches()) {
				UserVO userVO = this.getUser(id);
				result.add(userVO);
			}

		}
		return result;
	}

	@Override
	public void resetFaildLoginTimes(String id) {
		System.out.println("UserServiceImpl.resetFaildLoginTimes()>>>id=" + id);
		jedis.del("user:failedLoginTimes:" + id);
	}

	@Override
	public void cleanUserDeviceRels(String id) {

		// 当前用户创建家庭的家庭成员
		Set<String> members = jedis.smembers("family:" + id);

		// 当前用户绑定的设备
		Set<String> devices = jedis.smembers("u:" + id + ":devices");

		Transaction trans = jedis.multi();
		for (String member : members) {
			trans.hdel("user:family", member); // 删除家庭成员（成员找家主）
			for (String device : devices) {
				trans.srem("u:" + member + ":devices", device);
				trans.hdel("device:owner", device);
			}
		}

		trans.del("family:" + id); // 删除家庭成员（家主找成员列表）
		trans.del("u:" + id + ":devices");

		trans.exec();
	}

	@Override
	public void cleanMailRegister(String id) {
		System.out.println("UserServiceImpl.cleanMailRegister()>>>uid=" + id);
		jedis.hdel("user:emailavailable", id);
	}

	@Override
	public void delUser(String id) {
		String mail = jedis.hget("user:email", id);
		Transaction trans = jedis.multi();
		trans.hdel("user:email", id);
		trans.hdel("user:emailavailable", id);
		trans.hdel("user:family", id);
		trans.hdel("user:nick", id);
		trans.hdel("user:phone", id);
		trans.hdel("user:shadow", id);
		trans.hdel("user:mailtoid", mail);
		trans.del("user:mobileid:" + id);
		trans.del("user:failedLoginTimes:" + id);
		trans.del("u:" + id + ":devices");
		trans.del("family:" + id);
		trans.hdel("device:owner", id);
		trans.exec();
	}

	@Override
	public void destroy() {
		JedisHelper.returnJedis(jedis);
	}

}
