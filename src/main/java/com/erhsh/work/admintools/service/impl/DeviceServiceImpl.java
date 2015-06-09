package com.erhsh.work.admintools.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.erhsh.work.admintools.service.IDeviceService;
import com.erhsh.work.admintools.utils.JedisHelper;
import com.erhsh.work.admintools.vo.DeviceVO;

public class DeviceServiceImpl implements IDeviceService {

	private Jedis jedis = JedisHelper.getJedis();

	@Override
	public DeviceVO getDevice(String id) {
		DeviceVO result = new DeviceVO(id);
		String mac = jedis.hget("device:mac", id);
		String name = jedis.hget("device:name", id);
		String dv = jedis.hget("device:dv", id);
		String sn = jedis.hget("device:sn", id);
		String addr = jedis.hget("device:adr", id);
		String createStamps = jedis.hget("device:regtime", id);

		result.setId(id);
		result.setMac(mac);
		result.setName(name);
		result.setDv(dv);
		result.setSn(sn);
		result.setAddr(addr);
		result.setCreateStamps(createStamps);
		return result;
	}

	@Override
	public List<DeviceVO> getAllDevices() {
		List<DeviceVO> result = new ArrayList<DeviceVO>();
		Map<String, String> devMaps = jedis.hgetAll("device:mac");
		for (String id : devMaps.keySet()) {
			DeviceVO device = getDevice(id);
			result.add(device);
		}
		return result;
	}

	@Override
	public void destroy() {
		JedisHelper.returnJedis(jedis);
	}

	@Override
	public List<DeviceVO> getDevices(String target) {
		// TODO Auto-generated method stub
		return null;
	}

}
