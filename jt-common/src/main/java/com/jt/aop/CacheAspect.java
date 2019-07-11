package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.jt.anno.CaCheAnnotation;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Component
@Aspect
public class CacheAspect {

	//引入redis集群
	@Autowired(required=false)
	private JedisCluster jedis;

	@Around(value="annotation(cacheAnno)")
	private Object around(ProceedingJoinPoint joinPoint , CaCheAnnotation cacheAnno) {
		//获取key值
		String key = getkey(joinPoint,cacheAnno);
		Object object = getObject(joinPoint,cacheAnno,key);
		return object;
	}

	private Object getObject(ProceedingJoinPoint joinPoint, CaCheAnnotation cacheAnno, String key) {
		Object object = null;
		//判断用户缓存 读/更新
		switch (cacheAnno.cacheType()) {
		case FIND:
			object = findCache(joinPoint,cacheAnno,key);
			break;
		case UPDATE:
			object = updateCache(joinPoint,key);
			break;
		}
		return object;
	}


	private Object findCache(ProceedingJoinPoint joinPoint, CaCheAnnotation cacheAnno, String key) {
		Object object = null;
		String result = jedis.get(key);
		try {
			if(StringUtils.isEmpty(result)) {  //表示缓存中没有数据,则执行业务层方法
				object = joinPoint.proceed(); //执行proceed方法的作用是让目标方法执行
				String json = ObjectMapperUtil.toJson(result);
				jedis.set(key, json); //将数据保存到redis
				System.out.println("AOP查询业务层获取信息");
			} else {  //缓存中 无数据 直接返回数据
				MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
				Class<?> returnType = methodSignature.getReturnType();
				object = ObjectMapperUtil.toObject(result, returnType.getClass());
				System.out.println("AOP查询缓存");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return object;
	}


	private Object updateCache(ProceedingJoinPoint joinPoint, String key) {
		Object object = null;
		//如果是更新操作，则直接删除缓存
		try {
			jedis.del(key);
			joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return object;
	}

	//类名.方法名.参数名称.值
	private String getkey(ProceedingJoinPoint joinPoint, CaCheAnnotation cacheAnno) {
		String targetClassName = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();

		//转化为方法对象
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = methodSignature.getParameterNames();
		String paramName = parameterNames[cacheAnno.index()];
		Object arg = joinPoint.getArgs()[cacheAnno.index()];	
		//类名.方法名.参数名称.值
		String key = targetClassName+"."+methodName+"."+paramName+"."+arg; 

		return key;
	}

}

























