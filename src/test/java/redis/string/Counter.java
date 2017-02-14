package redis.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.RedisAPI;
import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Counter {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	private static Jedis cacheJedis;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	private static byte[] LOCK = new byte[0];
	
	public static Jedis getInstance(RedisCacheManager cacheManager){
		if(cacheJedis != null){
			return ((RedisCache) cacheManager.getCache("default")).getCache();
		}
			if(cacheJedis == null){
				cacheJedis = ((RedisCache) cacheManager.getCache("default")).getCache();
			}
		return cacheJedis;
	}
	
	//redis 实现并发计数器,防止并发请求
	public void counter(String telphone,Jedis cacheJedis){
		String redisKey = "SMS_LIMIT_" + telphone;
		try {
			//long count = cacheJedis.incr(redisKey);
			long count = redisTemplate.boundValueOps(redisKey).increment(1);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		//Transaction tx = cacheJedis.multi();
		    /*for (int i = 0; i < 100000; i++) {
		        tx.set("t" + i, "t" + i);
		    }
		    List<Object> results = tx.exec();*/
		//Response<Long> count = tx.incrBy(redisKey, 1);
		/*if (count == 1) {
			//设置有效期一分钟
			cacheJedis.expire(redisKey, 60);
		}
		if (count > 1) {
			System.out.println("每分钟只能发送一次短信");
			return;
		}*/
	}
	
	@Test
	public void testRedisCounter() {
		final JedisPool pool = RedisAPI.getPool("10.200.11.153", 6379,"123456");
		final String redisKey = "SMS_LIMIT_" + "sd32322323231ds";
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Jedis jedis = null;
					try {
						jedis = pool.getResource();
						/*boolean flag = true;
						while(true){
							//jedis.watch(redisKey);
							Transaction multi = jedis.multi();
							multi.incrBy(redisKey, 1);
							List<Object> exec = multi.exec();  
							String ret = String.valueOf(exec.get(0));
							if (ret == null || ret.equals("UNLOCK")) {
								 flag = true;
				            }else{
				            	flag = false;
				            }
							//System.out.println(jedis.incr(redisKey));
							//jedis.unwatch();
							System.out.println(jedis.get(redisKey));
						}*/
						//System.out.println(jedis.incr(redisKey));
						RedisAPI.counter();
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						//pool.returnResource(jedis);
					}
				}
			}).start();
		}
		//pool.destroy(); 
	}
}
