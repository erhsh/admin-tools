package com.erhsh.work.admintools.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.erhsh.work.admintools.service.IDeviceService;
import com.erhsh.work.admintools.utils.JedisHelper;
import com.erhsh.work.admintools.vo.DeviceVO;
import com.erhsh.work.admintools.vo.UserVO;

public class DeviceServiceImpl implements IDeviceService {
	private static final Logger LOG = LoggerFactory
			.getLogger(DeviceServiceImpl.class);

	private Jedis jedis = JedisHelper.getJedis();

	@Override
	public DeviceVO getDevice(String id) {

		LOG.debug("get device {}", id);

		DeviceVO result = new DeviceVO(id);
		String mac = jedis.hget("device:mac", id);
		if (null == mac) {
			return result;
		}

		String mac2 = jedis.hget("device:mac2", id);
		String name = jedis.hget("device:name", id);
		String dv = jedis.hget("device:dv", id);
		String sn = jedis.hget("device:sn", id);
		String addr = jedis.hget("device:adr", id);
		String createStamps = jedis.hget("device:regtime", id);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null != createStamps) {
			createStamps = format.format(new Long(createStamps));
		}

		result.setId(id);
		result.setMac(mac);
		result.setMac2(mac2);
		result.setName(name);
		result.setDv(dv);
		result.setSn(sn);
		result.setAddr(addr);
		result.setCreateStamps(createStamps);

		result.setOwner(getUser(id));
		return result;
	}

	public UserVO getUser(String id) {
		UserVO result = new UserVO();
		String owner = jedis.hget("device:owner", id);
		if (null == owner) {
			LOG.debug("Device {} owner is empty", id);
			return result;
		}

		// 用户信息
		String email = jedis.hget("user:email", id); // 邮箱
		String nick = jedis.hget("user:nick", id); // 昵称

		result.setId(id);
		result.setLoginName(email);
		result.setNick(nick);

		return result;
	}

	@Override
	public List<DeviceVO> getDevices(String target) {

		LOG.info("Search Device: cond={}", target);

		List<DeviceVO> result = new ArrayList<DeviceVO>();

		DeviceVO deviceVO = getDevice(target);

		if (!deviceVO.isEmpty()) {
			result.add(deviceVO);
			return result;
		}

		Map<String, String> devMaps = jedis.hgetAll("device:mac2");
		for (Entry<String, String> entrySet : devMaps.entrySet()) {
			String key = entrySet.getKey();
			String value = entrySet.getValue();

			if (value.contains(target)) {
				DeviceVO device = getDevice(key);
				result.add(device);
			}
		}

		return result;
	}

	@Override
	public void resetDeviceOwner(String deviceId) {
		LOG.debug("resetDeviceOwner deviceId {}", deviceId);
		jedis.hdel("device:owner", deviceId);
	}

	@Override
	public void destroy() {
		JedisHelper.returnJedis(jedis);
	}

}
