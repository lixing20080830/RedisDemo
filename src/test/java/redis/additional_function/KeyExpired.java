package redis.additional_function;

import java.util.HashMap;
import java.util.Map;

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
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class KeyExpired {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	/*****************************基本操作*********************************start***/
	@Test
	/**
	 * 1.设置生存时间
	 * EXPIRE 和 PEXIRE 的作用是让键在 N 秒钟或者 N 毫秒之后被删除。
	 * 带有生存时间的键就像是一个倒计时器，它会倒数 5、4、3、2、1、0，然后被删掉
	 * expire key seconds 将键key的生存时间设置为指定的秒数，o(1)
	 * pexpire key millseconds 将键key的生存时间设置为指定的毫秒数，o(1)
	 * 如果给定的键不存在，那么 expire 和 pexpire 将返回 0 ，表示设置失败；如果命令返回 1 ，那么表示设置成功
	 * 当一个键被设置了生存时间之后，它的生存时间将会随着时间的流逝而减少：时间过去一毫秒，键的生存时间就减少一毫秒；时间过去一秒钟，键的生存时间就减少一秒钟；以此类推。
	 * 当一个键的生存时间被减少至低于 0 时，Redis 就会自动将这个键删除掉
	 */
	public void expire(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		//System.out.println(cacheJedis.setex("key_expire",10,"hello world"));
		//System.out.println(cacheJedis.psetex("key_expire",2000,"hello world"));
		System.out.println(cacheJedis.get("key_expire"));
	}
	
	@Test
	/**
	 * 2.设置过期时间
	 * EXPIREAT 和 PEXPIREAT 的作用则是让键在指定的 UNIX 时间到达之后被删除
	 * 带有过期时间的键则像是一个定时器，它会在指定的时间来临之后被删掉
	 * expireat key timestamp 将键key的过期时间设置为指定的秒级unix时间戳，o(1)
	 * pexpireat key millisecondstimestamp 将键key的生存时间设置为指定的毫秒级unix时间戳，o(1)
	 * 如果给定的键不存在，那么 expireat 和 pexpireat 将返回 0 ，表示设置失败；如果命令返回 1 ，那么表示设置成功
	 * 对于被设置了过期时间的键来说，当键的过期时间小于当前时间的时候，Redis 就会自动地删除该键
	 */
	public void expireat(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		//System.out.println(cacheJedis.set("expireat_test1","hello world"));
		//System.out.println(cacheJedis.expireAt("expireat_test1",System.currentTimeMillis()+5));
		//System.out.println(cacheJedis.psetex("key_expire",2000,"hello world"));
		System.out.println(cacheJedis.get("expireat_test1"));
	}
	
	@Test
	/**
	 * 3.查看键的剩余生存时间
	 * 在为一个键设置过期时间或者生存时间之后，用户可以使用 TTL 命令或者 PTTL 命令查看这个键的剩余生存时间，以此来获知键还有多久才会被 Redis 删除
	 * ttl key 以秒为单位，返回键的剩余生存时间。 O(1)
	 * pttl key 以毫秒为单位，返回键的剩余生存时间。 O(1)
	 * 返回值-2 ：键不存在，-1：键存在，没有设置过期时间或者生存时间，>=0:键的剩余生存时间
	 */
	public void ttl(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		//System.out.println(cacheJedis.setex("key_expire3",10,"hello world"));
		System.out.println(cacheJedis.ttl("key_expire3"));
		System.out.println(cacheJedis.pttl("key_expire3"));
	}
	
	@Test
	/**
	 * 4.移除键的过期时间或者生存时间
	 * persist key 
	 * 移除为键 key 设置的过期时间或生存时间，使得它不会被 Redis 自动删除。移除成功时命令返回 1 ；如果命令没有设置过期时间或生存时间，那么命令返回 0 。
	 * 复杂度为 O(1)
	 */
	public void persist(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		System.out.println(cacheJedis.setex("key_expire4",10,"hello world"));
		System.out.println(cacheJedis.ttl("key_expire4"));
		System.out.println(cacheJedis.pttl("key_expire4"));
		System.out.println(cacheJedis.persist("key_expire4"));
	}
}