package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 实现思路:
	 * 	param:用户输入内容
	 * 	type: 1 2 3 判断不同的输入类型 可选参数1 username、2 phone、3 email
	 * 	
	 *	先将 1 2 3 转化为具体的数据库 字段
	 * 	校验原则:存在true  不存在返回false
	 */
	@Override
	public Boolean findCheckUser(String param, Integer type) {
		String column = null;
		switch (type) {
		case 1:
			column = "username";
			break;
		case 2:
			column = "phone";
			break;
		case 3:
			column = "email";
			break;
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		Integer count = userMapper.selectCount(queryWrapper);
		//返回数据true用户已存在，false用户不存在
		return count == 0 ? false : true;
		//测试网址：http://sso.jt.com/user/check/admin123/1?callback=666 
	}

}
