package com.erhsh.work.admintools.service.impl;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.erhsh.work.admintools.vo.UserVO;
import com.google.gson.Gson;

public class UserServiceImplTest {

	UserServiceImpl service = null;

	@Before
	public void setUp() throws Exception {
		service = new UserServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
		service.destroy();
	}

	@Test
	public void test() {
		List<UserVO> users = service.getUsers("zpei");

		Gson gson = new Gson();
		String str = gson.toJson(users);
		System.out.println(str);
		assert (users.size() != 0);
	}

}
