package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service                   
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	
	//redis是可以使用Jedis(单机)和JedisCluster(集群)两个方式
	@Autowired
	private JedisCluster jedisCluster;
	
	@Override
	public void saveUser(User user) {
		String md5pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5pass);
		user.setEmail(user.getPhone());
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		userMapper.insert(user);
	}

	/**
	 * 步骤:
	 * 	1.根据用户名和密码查询数据库
	 *  2.生成秘钥token串  MD5加密
	 *  3.把用户对象转化为json
	 *  4.将数据保存到redis中.
	 *  账号 admin123456  密码admin123123
	 */
	@Override
	public String findUserByUP(User user) {
		//1 密码加密
		String md5pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		//2 根据用户信息查询数据库
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", user.getUsername());
		queryWrapper.eq("password", md5pass);
		User userDB = userMapper.selectOne(queryWrapper);
		//3 判断用户是否正确
		if (userDB == null) {
			return null;
		}
		String token = "JT_TICKEY" + System.currentTimeMillis() + user.getUsername();
		token = DigestUtils.md5DigestAsHex(token.getBytes());
			//脱敏处理
		userDB.setPassword("****---****");
		String userJson = ObjectMapperUtil.toJson(userDB);
		jedisCluster.setex(token, 7*24*3600, userJson);
		return token;
	}

}




























