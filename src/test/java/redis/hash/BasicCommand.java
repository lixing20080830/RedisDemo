package redis.hash;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.RedisAPI;
import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * @author mingyangyang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class BasicCommand {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	/*****************************基本操作*********************************start***/
	@Test
	/**
	 * 1.关联域值对
	 * hset key field value
	 * 复杂度为 O(1) 
	 */
	public void hset(){
		JedisPool pool = RedisAPI.getPool("10.200.11.153", 6379,"123456");
		Jedis jedis = pool.getResource();
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field没有关联值，命令返回1，如果有，命令新值覆盖旧值，返回0
//		System.out.println(cacheJedis.hset("message", "id", "123456"));
//		System.out.println(cacheJedis.hset("message", "send", "111111"));
//		System.out.println(cacheJedis.hset("message", "revicer", "222222"));
		System.out.println(jedis.hset("icon1", "中国队", "大叔大叔"));
		System.out.println(jedis.hget("icon1", "中国队"));
	}
	
	@Test
	/**
	 * 2.获取域关联的值
	 * hget key field
	 * 复杂度为 O(1) 
	 */
	public void hget(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field没有关联值，返回nil
		System.out.println(cacheJedis.hget("message", "id"));
		System.out.println(cacheJedis.hget("message", "send"));
		System.out.println(cacheJedis.hget("message", "revicer"));
	}
	
	@Test
	/**
	 * 3.仅当域不存在时，关联域值对
	 * hsetnx key field
	 * 复杂度为 O(1) 
	 */
	public void hsetnx(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field已有与之关联的值，则不做任何操作，返回0，反之则返回为1
		System.out.println(cacheJedis.hsetnx("message", "id", "123456"));
		System.out.println(cacheJedis.hsetnx("message", "send", "111111"));
		System.out.println(cacheJedis.hsetnx("message", "revicer", "222222"));
	}
	
	@Test
	/**
	 * 4.检查域是否存在
	 * hexistis key field
	 * 检查键key中，给定域field是否存在，命令存在返回1，不存在返回0
	 * 复杂度为 O(1) 
	 */
	public void hexistis(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.hexists("message", "id"));
		System.out.println(cacheJedis.hexists("message", "revicer"));
	}
	
	@Test
	/**
	 * 5.删除给定域值对
	 * hdel key field
	 * 删除键key中的一个或者多个指定域，以及那些域的值，不存在的域将被忽略
	 * 复杂度为 O(n),n为被删除的域值对数量 
	 */
	public void hdel(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.hdel("message", "id","sender"));
	}
	
	@Test
	/**
	 * 6.获取散列键key包含的域值对数量
	 * hlen key
	 * 复杂度为 O(1) 
	 */
	public void hlen(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.hlen("message"));
	}
	/*****************************基本操作*********************************end***/
	
	/*****************************批量操作*********************************start***/
	@Test
	/**
	 * 7.一次设置和获取散列中的多个域值对
	 * hmset key field value [field value...]
	 * 复杂度为 O(n),域值对数量 
	 */
	public void hmset_and_hmget(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		Map<String, String> fieldMap  = new HashMap<String,String>();
		fieldMap.put("id", "10086");
		fieldMap.put("sender", "zhangsan");
		fieldMap.put("recevier", "lisi");
		System.out.println(cacheJedis.hmset("message",fieldMap));
		System.out.println(cacheJedis.hmget("message","id","sender","recevier"));
	}
	
	@Test
	/**
	 * 8.获取散列包含的所有域、值、或者域值对
	 * hmset key field value [field value...]
	 * 复杂度为 O(n),域值对数量 
	 */
	public void hgetall(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		//1.返回键key中包含的所有域，复杂度o(n),n为返回域的数量
		//备注：为什么命令叫 HKEYS 而不是 HFIELDS ？对于散列来说，key 和 field 表示的是同一个意思，并且 key 比 field 更容易拼写，所以 Redis 选择使用 HKEYS 来做命令的名字，而不是 HFIELDS 。
		System.out.println(cacheJedis.hkeys("message"));
		//2.返回键key中包含的所有域的值，复杂度o(n),n为返回值的数量
		System.out.println(cacheJedis.hvals("message"));
		//3.返回键key包含所有的域值对，复杂度o(n),n为返回域值对的数量
		System.out.println(cacheJedis.hgetAll("message"));
	}
	/*****************************批量操作*********************************end***/
	
	
	/*****************************数字操作*********************************start***/
	//和字符串键的值一样，在散列里面，域的值也可以被解释为数字，并执行相应的数字操作,不在写示例了
	/*****************************数字操作*********************************end***/
	
	@Test
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
}