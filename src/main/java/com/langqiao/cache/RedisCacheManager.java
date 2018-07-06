package com.langqiao.cache;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCacheManager extends AbstractCacheManager{
	private Collection<? extends Cache> caches;
	
	public void setCaches(Collection<? extends Cache> caches) {
		this.caches = caches;
	}
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		int i = 0;
		
		JedisPoolConfig config =  new  JedisPoolConfig();
		config.setMaxTotal(500);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(100*100);
		config.setTestOnBorrow(false);
	 	          
		JedisPool pool = new JedisPool(config,  "106.12.37.42" );
		for (Cache cache : caches){
			Jedis jedis = pool.getResource();
			jedis.auth("lixing");
			jedis.select(i++);
			
			RedisCache rc = (RedisCache)cache;
			rc.setCache(jedis);
		}
		
		return this.caches;
	}
}
