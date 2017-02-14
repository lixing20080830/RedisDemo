package redis.list.application.messageQueue;

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
	}
	
	@Test
	public void enqueue() {
		MessageQueue messageQueue = new MessageQueue("message_queue");
		try {
			for(int i = 0;i < 10;i++){
				messageQueue.enqueue(new Message(i,"zhangsan"+i));
			}
			messageQueue.get_all_items();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void del_queue_key(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		jedis.del("message_queue");
	}
	
	@Test
	public void get_all_items(){
		MessageQueue messageQueue = new MessageQueue("message_queue");
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis jedis = redisCache.getCache(); 
		messageQueue.get_all_items();
	}
}
