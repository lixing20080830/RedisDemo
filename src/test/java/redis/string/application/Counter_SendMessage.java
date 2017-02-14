package redis.string.application;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;
import com.langqiao.util.RedisUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Counter_SendMessage {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	private static Jedis cacheJedis;
	
	private static JedisPool pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
	
	public Counter_SendMessage(){
		
	}
	
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
	//这里有风险，incrBy和expire2个命令不在一个事务里，不具有原子性
	public static void counter(){
		String redisKey = "TEST_SMS_LIMIT_" + "1234567891113333";
    	Jedis jedis = null;
    	try {
    		jedis = pool.getResource();
    		long count = jedis.incrBy(redisKey, 1);
    		if (count == 1) {
    			System.out.println("短信发送成功..............");
    			//设置有效期一分钟
    			jedis.expire(redisKey, 60);
    		}
    		if (count > 1) {
    			System.out.println(("retMsg:"+"每分钟只能发送一次短信"));
    			return;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//redis 实现并发计数器,防止并发请求
	//这里有风险，setnx和expire2个命令不在一个事务里，不具有原子性
	public static void counter1(){
		String redisKey = "TEST_SMS_LIMIT_" + "1234567891113333dddd";
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			//设置成功
			long opt = jedis.setnx(redisKey,"test");
			if (opt == 1) {
				System.out.println("短信发送成功..............");
				//设置有效期一分钟
				jedis.expire(redisKey, 60);
			}
			if (opt == 0) {
				System.out.println(("retMsg:"+"每分钟只能发送一次短信"));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//redis 实现并发计数器,防止并发请求
	public static void counter2(){
		String redisKey = "TEST_SMS_LIMIT_" + "1234567891113333dddddd111222ddd111";
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			//设置成功
			if (!jedis.exists(redisKey)) {
				System.out.println("短信发送成功..............");
				String opt = jedis.set(redisKey,"test");
				//设置有效期一分钟
				jedis.expire(redisKey, 60);
			}
			if (jedis.exists(redisKey)) {
				System.out.println(("retMsg:"+"每分钟只能发送一次短信"));
				return;
			}
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
						counter2();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
