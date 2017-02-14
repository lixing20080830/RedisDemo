package redis.set;

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
 * 1.redis的集合是以无序的方式存储多个各不相同的元素
 * 2.用户可以快㏿地向集合添加元素，或者从集合里面 删除元素，也可以对多个集合进行集合运算操作，比如计算并集、交集和差集
 * 3.不要使用集合来储存有序的数据。如果想要 储存有序且重复的值，可以使用列表；如果想要储存有序且无重复的值，可以使用之后介绍的有序集合。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class BasicCommand {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	/*****************************元素操作(添加、删除、检查是否存在，返回集合大小)*********************************start***/
	@Test
	/**
	 * 1.添加元素
	 * sadd key elem [elem...]
	 * 将一个或多个元素添加到 给定的集合里面，已经存在于集合的元素会自 动被忽略，命令返回新添加到集合的元素数量
	 * 复杂度为 O(n) ,N 为成功添加的元素数量
	 */
	public void sadd(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.sadd("classmate", "zhangsan"));
		System.out.println(cacheJedis.sadd("classmate", "lisi", "wangwu"));
		System.out.println(cacheJedis.sadd("classmate", "lisi", "liuliu"));
	}
	
	@Test
	/**
	 * 2.移除
	 * srem key elem [elem...]
	 * 移除集合中的一个或者多个元素，不存在于集合中的元素会自 动被忽略命令返回存在并且被移除的元素数量
	 * 复杂度为 O(n),n为被移除元素的数量 
	 */
	public void srem(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.srem("classmate", "zhangsan"));
	}
	
	@Test
	/**
	 * 3.检查给定元素是否存在于集合
	 * sismember key elem
	 * 给定元素是否存在于集合，存在的话返回1，不存在的话或者给定的key不存在，那么返回0
	 * 复杂度为 O(1)
	 */
	public void sismember(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.sismember("classmate", "lisi"));
	}
	
	@Test
	/**
	 * 4.返回集合包含的元素数量(也即是集合的基数)
	 * scard key
	 * 复杂度为 O(1),Redis 会储存集合的长度，所以命令的复杂度为 O(1)
	 */
	public void scard(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.scard("classmate"));
	}
	
	@Test
	/**
	 * 5.返回集合包含的所以元素
	 * smembers key
	 * 复杂度为 O(n),n为集合数量
	 * 当集合的基数比较大时，执行这个命令有可能会造成服务器阻塞
	 */
	public void smembers(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.smembers("classmate"));
	}
	
	@Test
	/**
	 * 6.随机从集合中弹出一个元素
	 * spop key，会从集合中移除元素
	 * 复杂度为 O(1)
	 */
	public void spop(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.smembers("classmate"));
		System.out.println(cacheJedis.spop("classmate"));
		System.out.println(cacheJedis.smembers("classmate"));
	}
	
	@Test
	/**
	 * 7.随机从集合中返回元素,不会从集合中移除元素
	 * application:通过将参与抽奖的所有人都添加到一个集合里面，并使用SRANDMEMBER 命令来抽取获奖得主，我们也可以构建一个类似的抽奖功能
	 * srandmemeber key [count]
	 * 如果没有给定可选的 count 参数，那么命令随机地返回集合中的一个元素
	 * 当 count 为正数，并且少于集合基数 时，命令返回一个包含 count 个元素的数组，数组中的每个元素各不相同。如果 count 大于或等于集合基数，那么命令返回整个集合。
	 * 当 count 为负数时，命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为count的绝对值
	 * 复杂度为 O(n),n为被返回元素的数量
	 */
	public void srandmember(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.sadd("friend","tom","jack","jeery","john","harry"));
		//随机返回4个不重复的元素
		System.out.println(cacheJedis.srandmember("friend", 4));
		System.out.println(cacheJedis.srandmember("friend", 4));
		System.out.println(cacheJedis.srandmember("friend", 4));
		System.out.println(cacheJedis.srandmember("friend", 5));
		System.out.println(cacheJedis.srandmember("friend", 10));
		//随机返回4个可能重复的元素
		System.out.println(cacheJedis.srandmember("friend", -4));
		System.out.println(cacheJedis.srandmember("friend", -5));
	}
	/*****************************元素操作(添加、删除、检查是否存在，返回集合大小)*********************************end***/

	/*****************************集合运算操作(计算并集、交集、差积)********************************************end***/
	@Test
	/**
	 * 8.差集运算
	 * sdiff key [key ...]，计算所有给定集合的差集，并返回结果
	 * 复杂度为 O(n),n为所有参与差集计算的元素数量之和
	 */
	public void sdiff(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.sadd("number1","123","456","789"));
		System.out.println(cacheJedis.sadd("number2","123","456","777"));
		System.out.println(cacheJedis.sdiff("number1","number2"));
	}
	
	@Test
	/**
	 * 9.差集运算
	 * sdiffstore destkey key [key ...]，计算所有给定集合的差集，并将结果存储到destkey
	 * 复杂度为 O(n),n为所有参与差集计算的元素数量之和
	 */
	public void sdiffstore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.sadd("number1","123","456","789"));
		System.out.println(cacheJedis.sadd("number2","123","456","777"));
		System.out.println(cacheJedis.sdiff("number1","number2"));
		System.out.println(cacheJedis.sdiffstore("number3","number1","number2"));
		System.out.println(cacheJedis.smembers("number3"));
	}
	
	@Test
	/**
	 * 10.交集运算
	 * application:使用集合实现共同关注功能,新浪微博的共同关注功能：当用 户访问另一个用户时，该功能会显示出两个用户关注了哪些相同的用户
	 * sinsert key [key ...]，计算所有给定集合的交集，并返回结果
	 * 复杂度为 O(n * m),n为给定集合当中基数最小的集合， m为给定集合的个数
	 */
	public void sinter(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//System.out.println(cacheJedis.sadd("number1","123","456","789"));
		//System.out.println(cacheJedis.sadd("number2","123","456","777"));
		System.out.println(cacheJedis.sinter("number1","number2"));
	}
	
	@Test
	/**
	 * 11.交集运算
	 * sinsert destkey key [key ...]，计算所有给定集合的交集，并将结果存储到destkey
	 * 复杂度为 O(n * m),n为给定集合当中基数最小的集合， m为给定集合的个数
	 */
	public void sinterstore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//System.out.println(cacheJedis.sadd("number1","123","456","789"));
		//System.out.println(cacheJedis.sadd("number2","123","456","777"));
		System.out.println(cacheJedis.sinterstore("number4","number1","number2"));
		System.out.println(cacheJedis.smembers("number4"));
	}
	
	@Test
	/**
	 * 12.并集运算
	 * sunion key [key ...]，计算所有给定集合的并集，并返回结果
	 * 复杂度为 O(n),n为参与计算的元素数量
	 */
	public void sunion(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//System.out.println(cacheJedis.sadd("number1","123","456","789"));
		//System.out.println(cacheJedis.sadd("number2","123","456","777"));
		System.out.println(cacheJedis.sunion("number1","number2"));
	}
	
	@Test
	/**
	 * 13.并集运算
	 * sunionstore destkey key [key ...]，计算所有给定集合的并集，并将结果存储到destkey
	 * 复杂度为 O(n),n为参与计算的元素数量
	 */
	public void sunionstore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//System.out.println(cacheJedis.sadd("number1","123","456","789"));
		//System.out.println(cacheJedis.sadd("number2","123","456","777"));
		System.out.println(cacheJedis.sunionstore("number5","number1","number2"));
		System.out.println(cacheJedis.smembers("number5"));
	}
	
	@Test
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
}