package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private JedisCluster jedisCluster;
	
	@Autowired
	private UserService userService;
	
	/**
	  * 前台通过jsonp形式实现跨域请求，后台需要特殊个数封装数据
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject findCheckUser(@PathVariable String param,@PathVariable Integer type, String callback) {
		Boolean flag = userService.findCheckUser(param,type);
		return new JSONPObject(callback, SysResult.ok(flag));
	}
	
	/**
	  *  根据token数据获取用户信息   在 index.html 主页面进行回显
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/query/{token}")
	public JSONPObject findUserByToken(@PathVariable String token ,String callback) {
		String userJson = jedisCluster.get(token);
		return new JSONPObject(callback, SysResult.ok(userJson));
	}
	
}
