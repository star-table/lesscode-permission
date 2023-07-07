package com.polaris.lesscode.permission.annotation;

import org.apache.ibatis.cache.CacheKey;

import java.lang.annotation.*;

/**
 * 自动缓存
 *
 * @author nico
 * @date 2020年1月19日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AutoCache {

	/**
	 * 缓存的key，支持占位符，例如 sys:user:${userId}或者sys:user:${user.id}, 自动取方法入参替换进去
	 * 
	 * @return
	 */
	String value();
	
	/**
	 * 当{@link CacheKey}的类型为Hash时，该字段对应子key
	 * 
	 * @return
	 */
	String hashKey() default "";
	
	/**
	 * 缓存失效时间， 默认不会失效， 单位：秒
	 * 
	 * @return
	 */
	int expire() default 0;
	
	/**
	 * 缓存抖动范围，默认不抖动；
	 * 防止缓存同一时间失效，可以定义一个抖动范围，在原有失效时间的基础上做增减来保证缓存不会在同一刻失效，造成cache压力过大。
	 * 
	 * @return
	 */
	int expireShakeRange() default 0;
	
	/**
	 * 缓存类型 {@link CacheType}
	 * @return
	 */
	CacheType type() default CacheType.STRING;


	public static enum CacheType {

		/**
		 * 字符串
		 */
		STRING,

		/**
		 * 哈希
		 */
		HASH
	}

}
