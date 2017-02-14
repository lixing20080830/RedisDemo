package redis.list.application.fixedFIFO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Producer {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	public static void main(String[] args) {
		FixedFIFO fixedFIFO = new FixedFIFO("fix_fifo_key", 10l);
		try {
			for(int i = 0;i < 20;i++){
				fixedFIFO.enqueue(new Message(i,"zhangsan"+i));
			}
			fixedFIFO.get_all_items();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void enqueue() {
		FixedFIFO fixedFIFO = new FixedFIFO("fix_fifo_key", 10l);
		try {
			for(int i = 11;i < 12;i++){
				fixedFIFO.enqueue(new Message(i,"zhangsan"+i));
			}
			fixedFIFO.get_all_items();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void del_queue_key(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		jedis.del("fix_fifo_key");
	}
	
	@Test
	public void get_all_items(){
		FixedFIFO fixedFIFO = new FixedFIFO("fix_fifo_key", 10l);
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		fixedFIFO.get_all_items();
	}
}
