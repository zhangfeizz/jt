package com.jt.util;
/**
 *该对象保存的是用户信息
 */

import com.jt.pojo.User;

public class UserThreadLocal {
	//JVM 直接创建的对象  不会自己回收  
	private static ThreadLocal<User> thread = new ThreadLocal<>();
	
	public static void set(User user) {
		thread.set(user);
	}
			
	public static User get() {
		return thread.get();
	}
	
	//使用threadlocal必须注意内存泄漏
	public static void remove() {
		thread.remove();
	}
}
