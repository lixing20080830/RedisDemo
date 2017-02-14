package redis.pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * @author mingyangyang
 * redis-流水线
 * 通过减少客户端与服务器之间的通信次数来提高程序的 执行效率
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class PipelineTest {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Test
	public void test(){
		none_pipeline_hmset();
		pipeline_hmset();
		none_pipeline_hmget();
		pipeline_hmget();
	}
	@Test
	/**
	 *  测试redis的hmset，不用流水线所花费的时间
	 */
	public void none_pipeline_hmset(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		jedis.select(1);
		jedis.flushDB();
		long start = System.currentTimeMillis();
		Map<String, String> data = new HashMap<String,String>();
		for(int i=0;i<100000;i++){
			data.clear();
			data.put("k_"+i, "v_"+i);
			jedis.hmset("key_"+i, data);
		}
		long end = System.currentTimeMillis();
		System.out.println("dbsize:"+jedis.dbSize());
		System.out.println("hmset without pipeline总共花费的时间为："+(end-start)+"毫秒");
	}
	
	@Test
	/**
	 *  
	 */
	public void pipeline_hmset(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		jedis.select(1);
		jedis.flushDB();
		Pipeline p = jedis.pipelined();
		long start = System.currentTimeMillis();
		Map<String, String> data = new HashMap<String,String>();
		for(int i=0;i<100000;i++){
			data.clear();
			data.put("k_"+i, "v_"+i);
			p.hmset("key_"+i, data);
		}
		p.sync();
		long end = System.currentTimeMillis();
		System.out.println("dbsize:"+jedis.dbSize());
		System.out.println("hmset with pipeline总共花费的时间为："+(end-start)+"毫秒");
	}
	
	@Test
	/**
	 *  测试redis的hmget，不用流水线所花费的时间
	 */
	public void none_pipeline_hmget(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		jedis.select(1);
		Set<String> keys = jedis.keys("*");
		Map<String, Map<String, String>> result = new HashMap<String,Map<String,String>>();
		long start = System.currentTimeMillis();
		for(String key : keys){
			result.put(key, jedis.hgetAll(key));
		}
		long end = System.currentTimeMillis();
		System.out.println("resultsize:"+result.size());
		System.out.println("hmget without pipeline总共花费的时间为："+(end-start)+"毫秒");
	}
	
	@Test
	/**
	 *  测试redis的hmget，使用流水线所花费的时间
	 */
	public void pipeline_hmget(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		Map<String, Map<String, String>> result = new HashMap<String,Map<String, String>>();
		Map<String, Response<Map<String, String>>> responses = new HashMap<String,Response<Map<String, String>>>();
		jedis.select(1);
		Pipeline p = jedis.pipelined();
		long start = System.currentTimeMillis();
		Set<String> keys = jedis.keys("*");
		for(String key : keys){
			responses.put(key, p.hgetAll(key));
		}
		p.sync();
		for(String key:responses.keySet()){
			result.put(key, responses.get(key).get());
		}
		long end = System.currentTimeMillis();
		System.out.println("resultsize:"+result.size());
		System.out.println("hmget with pipeline总共花费的时间为："+(end-start)+"毫秒");
	}
}