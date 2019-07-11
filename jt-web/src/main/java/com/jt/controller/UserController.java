package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/{moduleName}")
	public String moduleName(@PathVariable String moduleName) {
		return moduleName;
	}

	@Reference(timeout=3000,check=false)  //Controller引入dubbo接口
	private DubboUserService userService;
	

	//实现用户新增
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	/**实现用户单点登录.
	 * 
	 * 返回值要求:
	 * 	如果用户名或密码错误.则token为null
	 *  如果用户填写正确, token中有数据
	 * 业务流程:
	 * 	1.判断数据是否有效
	 *  2.如果有效则应该将数据保存到cookie中
	 *  
	 * 关于Cookie补充知识:
	 * 	1.setPath("/")
	 * 		只要是jt项目中的页面都可以访问这个cookie
	 * 	 .setPath("/sso/")
	 * 		只有位于sso/下的页面才可以访问这个cookie
	 * 	 
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user, HttpServletResponse response) {
		try {
			String token = userService.findUserByUP(user);
			if (!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICEKT", token);
				cookie.setMaxAge(7*24*3600);
				cookie.setPath("/");
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}

	/*
	 * 实现用户的登出操作   删除redis：key -- token/ cookie
	 *  1 、先获取用户浏览器的cookie数据
	 *  2 、根据cookie删除redis
	 *  3 、删除cookie
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String token =null;
		//1 获取cookie数据
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//2 删除redis
		jedisCluster.del(token);
		//3 删除cookie 设置生命周期
		Cookie cookie = new Cookie("JT_TICKET", token);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:/index.html";
	}
	
	
}





































