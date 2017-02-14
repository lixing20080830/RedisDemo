package redis.additional_function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * @author mingyangyang
 * Redis 的发布与订阅功能可以让用户将消息同时发送给多个客户端
 * 发布者（publisher）：发布消息的客户端。
 * 频道（channel）：构建在服务器内部，负责接收发布者发送的消息，并将消息转发给频道的订阅者。
 * 模式（pattern）：构建在服务器内部，负责对频道进行匹配，当被匹配的频道接到消息时，模式也会将消息转发给模式的订阅者。
 * 订阅者（subscriber）：通过订阅频道或者模式来获取消息的客户端。每个频道或者模式都可以有任意多个订阅者
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class PublishSubscribe {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	final MyListener listener = new MyListener(); 
	
	/*****************************基本操作*********************************start***/
	@Test
	/**
	 * 1.订阅频道
	 * subscribe channel [channel ...]
	 * 订阅给定的一个或多个频道
	 * 复杂度为 O(N) ， N 为被订阅频道的数量
	 * 2.PSUBSCRIBE pattern [pattern ...] 订阅一个或多个模式，pattern 参数可以包含 glob 风格的匹配符
	 * 复杂度为 O(N) ，N 为被订阅模式的数量
	 */
	public void subscribe(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		//cacheJedis.subscribe(new MyListener(),"news::it");
		cacheJedis.psubscribe(listener,new String[]{"news::*"});
		cacheJedis.publish("news::it","hello world" );
	}
	
	@Test
	/**
	 * 2.取消订阅
	 * 复杂度为 O(N) ，N 为被订阅模式的数量
	 */
	public void unsubscribe(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		listener.punsubscribe(new String[]{"news::*"});
	}
	
	@Test
	/**
	 * 3.发布消息 发布消息 PUBLISH channel message
	 * 将消息发送至指定的频道，命令返回接收到消息的订阅者数量
	 * 复杂度为 O(N) ，N 为接收到消息的订阅者数量（包括通过订阅频道来接收消息的订阅者和通过订阅模式来接收消息的订阅者）
	 */
	public void publish(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.publish("news::it","hello world" );
		cacheJedis.publish("news::it","big big world" );
	}
	
	@Test
	/**
	 * 4.查看被订阅的频道
	 * PUBSUB CHANNELS [pattern] 列出目前至少有一个订阅者的频道。
	 * 如果给定了可选的 pattern 参数，那么只列出与模式相匹配的 频道。
	 * 复杂度为 O(N) ，N 为服务器中被订阅频道的总数量。
	 */
	public void pubsub(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.publish("news::it","big big world" );
	}
	
}