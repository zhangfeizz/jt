package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

@Component
public class UserInterceptor implements HandlerInterceptor {
	@Autowired
	private JedisCluster jedisCluster;

	//完成用户校验，如果没有登录 则跳转用户登录页面
	//如果用户已登录则放行
		//boolean  true放行  false 拦截+重定向
	/*
	 * 用户拦截器实现步骤：
	 * 	1.获取用户的cookie数据
	 *  2.判断用户是否已经登录
	 *  	没有登录，重定向到登录页面
	 *  	已经登录，判断redis中是否有数据
	 *  			   有数据  表示用户之前登录成功 放行
	 *  			   无数据  表示登录失败，重定向到用户登录页面
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		
		if (!StringUtils.isEmpty(token)) {
			//判断redis是否有数锯
			String userJson = jedisCluster.get(token);
			if (!StringUtils.isEmpty(userJson)) {
				//程序执行到这里表示用户已经登录
				User user = ObjectMapperUtil.toObject(userJson, User.class);
				UserThreadLocal.set(user);
				System.out.println("拦截器执行");
				return true;
			}
		}
		response.sendRedirect("/user/login.html");
		return false;
	}
	
	//controller方法执行结束后执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	//视图渲染完成之后执行
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove(); //删除线程  
		System.out.println("线程go over");
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
