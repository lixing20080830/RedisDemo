package redis.hash.application;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;
import com.langqiao.util.RedisUtil;

/**
 * 使用散列键重新实现计数器
 * @author mingyangyang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Counter {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	private static Jedis cacheJedis;
	
	public static Jedis getInstance(RedisCacheManager cacheManager){
		if(cacheJedis != null){
			return ((RedisCache) cacheManager.getCache("default")).getCache();
		}
		if (cacheJedis == null) {
			cacheJedis = ((RedisCache) cacheManager.getCache("default")).getCache();
		}
		return cacheJedis;
	}
	
	//redis 实现并发计数器,防止并发请求
	public static void counter(){
		String redisKey = "test_hash_counter_bbbbbbbbbcccc";
		JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
    		jedis = pool.getResource();
			System.out.println(jedis.hincrBy("test_hash_key", "hash_field", 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						counter();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
