package com.jt.vo;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表示页面统一的返回方法
 *  status 200 业务正确
 *  	   201 业务错误			
 *  msg    服务端返回的提示信息
 *  data   服务端返回的数据
 */

@Data
@Accessors(chain=true)
public class SysResult {

	private Integer status;
	private String msg;
	private Object data;

	public SysResult() {}

	public SysResult(Integer status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	//定义成功的静态方法.
	public static SysResult ok(String msg,Object data) {
		return new SysResult(200, msg, data);
	}

	public static SysResult ok() {
		return new SysResult(200, null, null);
	}

	public static SysResult ok(Object data) {
		return new SysResult(200, null, data);
	}
	//定义失败的静态方法
	public static SysResult fail(String msg,Object data) {

		return new SysResult(201, msg, data);
	}

	public static SysResult fail() {

		return new SysResult(201, null, null);
	}
}
































