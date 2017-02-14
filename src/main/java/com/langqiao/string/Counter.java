package com.langqiao.string;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import com.langqiao.cache.RedisCacheManager;

public class Counter {
	
	@Autowired
	private static RedisCacheManager cacheManager;
	
	public static void main(String[] args) {
		Jedis cacheJedis = (Jedis) cacheManager.getCache("default");
		cacheManager.getCache("default").put("test", "test123");
		//System.out.println(cacheManager.getCache("default").get("test123"));
		String smsPhone = "1234";
		String redisKey = "SMS_LIMIT_" + smsPhone;
		long count = cacheJedis.incrBy(redisKey, 1);
		if (count == 1) {
			//设置有效期一分钟
			cacheJedis.expire(redisKey, 60);
		}
		if (count > 1) {
		//resultMap.put("retCode", "-1");     resultMap.put("retMsg", "每分钟只能发送一次短信");
		//outPrintJson(resultMap);
		return;
		}
	}
}
