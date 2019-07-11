package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

@Service
public class HttpClientService {
	
	@Autowired
	private CloseableHttpClient httpClient;
	
	@Autowired
	private RequestConfig requestConfig;
	
	
	public String doGet(String url) {
		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params) {
		return doGet(url, params, null);
	}
	
	
	/**
	 * 为了用户请求方便封装doGet方法.
	 * 参数说明:
	 * 	1.String url地址
	 *  2.map<String,String>集合封装参数
	 *  3.字符集编码
	 * 
	 * GETURl参数问题:
	 * 	http://localhost:8092/addUser?id=1&name=tomcat
	 *
	 */
	public String doGet(String url, Map<String, String> params, String charset) {
		//1 如果为null，则设定默认的字符集
		if (StringUtils.isEmpty(charset)) {
			charset="utf-8";
		}
		//2.判断用户是否传递参数
			//url:localhost:8092/addUser?id=value
		if (params != null) {
			//如果参数不为null,则需要url地址拼串?id=1&name=tomcat&
			url = url + "?";
			//需要获取参数变量map集合.
			for (Map.Entry<String, String> entry : params.entrySet()) {
				url = url + "?" + entry.getKey() + "=" +entry.getValue() + "&" ;
			}
			url =url.substring(0, url.length()-1);
		}
		//3.封装请求对象 HttpGet
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		System.out.println(httpGet);
		//4.发起http请求
		String result =null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode()==200) {
				result = EntityUtils.toString(response.getEntity(), charset);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}
	
	
	public String doPost(String url,Map<String,String> params,String charset) {
		String result=null;
		// 1.定义请求类型
		HttpPatch post = new HttpPatch(url);
		post.setConfig(requestConfig); //定义超时时间
		// 2.判断字符集
		if (StringUtils.isEmpty(charset)) {
			charset="utf-8";
		}
		//3.判断用户是否传递参数
		if (params != null) {
			List<NameValuePair> parameters = new ArrayList<>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,charset);//采用u8编码
				//将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//4.发送请求
		try {
			CloseableHttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode()==200) {
				result = EntityUtils.toString(response.getEntity(), charset);
			} else {
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String doPost(String url){

		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){

		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){

		return doPost(url, null, charset);
	}
}












































