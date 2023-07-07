package com.polaris.lesscode.permission.aspect;

import com.alibaba.fastjson.JSON;
import com.polaris.lesscode.permission.annotation.AutoCache;
import com.polaris.lesscode.permission.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 自动缓存切面
 *
 * @date 2020年1月19日
 */
@Aspect
@Component
@Slf4j
public class AutoCacheAspect {

	@Autowired
	private RedisConfig redisUtil;

	private static PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
			"${", "}", ".", false);

	@Pointcut("@annotation(com.polaris.lesscode.permission.annotation.AutoCache)")
	public void pointCut() {

	}

	@Around("pointCut()")
	public Object arround(ProceedingJoinPoint point) throws Throwable{
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		String[] methodArgNames = signature.getParameterNames();
		Object[] methodArgs = point.getArgs();
		Properties methodArgNameMap = new Properties();
		if(! ArrayUtils.isEmpty(methodArgNames)) {
			for(int index = 0; index < methodArgNames.length; index ++) {
				methodArgNameMap.setProperty(methodArgNames[index], String.valueOf(methodArgs[index]));
			}
		}

		AutoCache autoCache = method.getAnnotation(AutoCache.class);
		String cacheKey = autoCache.value();
		String hashKey = autoCache.hashKey();
		long expire = autoCache.expire();
		long expireShakeRange = autoCache.expire();
		if (expireShakeRange > 0 && expireShakeRange < expire) {
			expire = expire - (long)(Math.random() * expireShakeRange);	
		}
		
		AutoCache.CacheType cacheType = autoCache.type();

		cacheKey = helper.replacePlaceholders(cacheKey, methodArgNameMap);

		switch (cacheType){
		case STRING:
			return handleStringCache(point, method, cacheKey, expire);
		case HASH:
			throw new UnsupportedOperationException("不支持" + cacheType);
		}

		return  point.proceed();
	}

	public Object handleStringCache(ProceedingJoinPoint point, Method method, String cacheKey, long expire) throws Throwable{
		String cacheValue = (String) redisUtil.get(cacheKey);
		Object result = null;
		if(StringUtils.isBlank(cacheValue)) {
			result = point.proceed();
			if(result != null) {
				cacheValue = JSON.toJSONString(result);
				redisUtil.set(cacheKey, cacheValue);
				if (expire > 0){
					redisUtil.expire(cacheKey, expire, TimeUnit.SECONDS);
				}
				log.info("写缓存 key {}, value {} ", cacheKey, cacheValue);
			}else {
				log.info("为空不写缓存 key {}, value {}", cacheKey, cacheValue);
			}
		}else {
			if(! cacheValue.equalsIgnoreCase("NULL")){
				result = JSON.parseObject(cacheValue, method.getGenericReturnType());
			}
		}
		return result;
	}


}
