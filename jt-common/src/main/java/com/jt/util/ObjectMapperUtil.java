package com.jt.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 解决对象--》json
 * json--》对象
 */
public class ObjectMapperUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 对象 — —》  json
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		String json = null;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return json;
	}
	
	/**
	 * json--》对象
	 * @param <T>
	 * @param json
	 */
	public static <T> T toObject(String json, Class<T> target) {
		T t = null;
		try {
			t = mapper.readValue(json, target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return t;
	}
	
}
