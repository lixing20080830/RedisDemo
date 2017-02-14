package redis.zset;

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
 * 有序集合(sort set / zset)
 * 有序集合和集合一样，都可以包含任意数量的、各不相同的元素（ element），不同于集合的是，有序集
 * 合的每个元素都关联着一个浮点数格式的分 值（score），并且有序集合会按照分值，以从小到大的顺序来排列有序集合中的各个元素
 * 有序集合每个元素必须是不同的，但是元素的分值可以相同
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class BasicCommand {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	/*****************************基本操作(添加、删除、返回指定元素分值，返回集合包含的元素数量)*********************************start***/
	@Test
	/**
	 * 1.添加元素
	 * zadd key score element[[score element]...]
	 * 按照给定的分值和元素，将任意数量的元素添加到有序集合里面，命令的返回 值为成功添加的元素数量
	 */
	public void zadd(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zadd("fruits-price", 3.2 ,"branna"));
		System.out.println(cacheJedis.zadd("fruits-price", 2.0 ,"melon"));
		System.out.println(cacheJedis.zadd("fruits-price", 4.2 ,"apple"));
	}
	
	@Test
	/**
	 * 2.删除元素
	 * zrem key element[element...]
	 * 从有序集合中删除指定的元素，以及这些元素关联的分值，命令返回被成功删除的元素数量
	 */
	public void zrem(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrem("fruits-price","branna"));
	}
	
	@Test
	/**
	 * 3.返回元素的分值
	 * zscore key element
	 * 返回有序集合中，指定元素的分 值
	 */
	public void zscore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zscore("fruits-price", "apple"));
	}
	
	@Test
	/**
	 * 4.增加或减少元素的分值
	 * zincrby key increment element
	 * 为有序集合指定元素的分值加上增量 increment ，命令返回执行操作之后，元素的分值
	 * 没有相应的 ZDECRBY命令，但可以通过将 increment 设置为负数来减少分值
	 */
	public void zincrby(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zincrby("fruits-price",1,"apple"));
		System.out.println(cacheJedis.zincrby("fruits-price",-1,"apple"));
	}
	
	@Test
	/**
	 * 5.返回有序集合的基数
	 * zcard key
	 * 复杂度o(1) 
	 */
	public void zcard(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zcard("fruits-price"));
	}
	
	@Test
	/**
	 * 6.返回元素的排名（rank）
	 * zrank key element
	 * 返回指定元素在有序集合中的排名，其中 排名按照元素的分值从小到大计算,排名以 0 开始
	 * 复杂度o(1) 
	 */
	public void zrank(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrank("fruits-price","apple"));
		System.out.println(cacheJedis.zrank("fruits-price","melon"));
		System.out.println(cacheJedis.zrank("fruits-price","branna"));
	}
	
	@Test
	/**
	 * 7.返回元素的逆序排名（reverse rank）
	 * zrevrank key member
	 * 返回成员在有序集合中的逆序排名，其中 排名按照元素的分值从大到小计算,排名以 0 开始
	 * 复杂度o(1) 
	 */
	public void zrevrank(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrevrank("fruits-price","apple"));
		System.out.println(cacheJedis.zrevrank("fruits-price","melon"));
		System.out.println(cacheJedis.zrevrank("fruits-price","branna"));
	}
	/*****************************基本操作(添加、删除、返回指定元素分值，返回集合包含的元素数量)*********************************start***/
	
	/*****************************分值范围操作*********************************start*****************************/
	@Test
	/**
	 * 8.获取指定索引范围内的升序元素
	 * zrange key start stop [withscores]
	 * 返回有序集合在按照分值从小到大排列元素（升序排列） 的情况下，索引 start 至索引 stop 范围之内的所有元素
	 * 两个索引都可以是正数或者 负数。当给定 WITHSCORES 选项时，命令会将元素和分值一并返回
	 */
	public void zrange(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrange("fruits-price",0,2));
	}
	
	@Test
	/**
	 * 9.获取指定索引范围内的降序元素
	 * zrevrange key start stop [withscores]
	 * 返回有序集合在按照分值从大到小排列元素（降序排列） 的情况下，索引 start 至索引 stop 范围之内的所有元素
	 * 两个索引都可以是正数或者 负数。当给定 WITHSCORES 选项时，命令会将元素和分值一并返回
	 */
	public void zrevrange(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrevrange("fruits-price",0,2));
	}
	
	@Test
	/**
	 * 10.获取指定分值范围内的升序元素
	 * zrangebyscore key min max [withscores] [limit offset count]
	 * 返回有序集合中分值在min和max之间的所有元素
	 * 给定 WITHSCORES 选项时，元素和分值会一并返回。给定 LIMIT 选项时，可以通过 offset 参数指定返回的结果集要跳过多少个元素，而 count 参数则用于指定返回的元素数量。
	 */
	public void zrangebyscore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrangeByScore("fruits-price",3.0,7.0));
	}
	
	@Test
	/**
	 * 11.获取指定分值范围内的降序元素
	 * zrevrangebyscore key max min [withscores] [limit offset count]
	 * 返回有序集合中分值在min和max之间的所有元素
	 * 给定 WITHSCORES 选项时，元素和分值会一并返回。给定 LIMIT 选项时，可以通过 offset 参数指定返回的结果集要跳过多少个元素，而 count 参数则用于指定返回的元素数量。
	 */
	public void zrevrangebyscore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrevrangeByScore("fruits-price",7.0,3.0));
	}
	
	@Test
	/**
	 * 12.计算给定分值范围内的元素数量
	 * zcount key min max
	 * 返回有序集合分值在min和max之间的元素数量
	 * 给定 WITHSCORES 选项时，元素和分值会一并返回。给定 LIMIT 选项时，可以通过 offset 参数指定返回的结果集要跳过多少个元素，而 count 参数则用于指定返回的元素数量。
	 */
	public void zcount(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zcount("fruits-price",3.0,7.0));
	}
	
	@Test
	/**
	 * 13.移除指定排名范围内的升序排列元素
	 * zremrangebyrank key start stop
	 * 移除有序集合中，元素按升序 进行排列的情况下，指定排名范 围内的所有元素。
	 * 排名范围可以使用正数和负数
	 */
	public void zremrangebyrank(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zremrangeByRank("fruits-price",0,-1));
	}
	
	@Test
	/**
	 * 14.移除指定分值范围内的升序排列元素
	 * zremrangebyscore key min max
	 * 移除有序集合中，分值范围介于 min和 max之内的所有元素
	 */
	public void zremrangebyscore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.zrange("fruits-price",0,-1));
		System.out.println(cacheJedis.zremrangeByScore("fruits-price",3.0,7.0));
	}
	/*****************************分值范围操作*********************************start*****************************/
	
	
	/*****************************集合运算操作(计算并储存多个有序集合的交集、并集运算 结果)*********************************start*****************************/
	@Test
	/**
	 * 15.并集
	 * zunionstore destkey numkeys key [key...],相同的元素的分值会被累加
	 * numkeys 参数指定要进行计算的有序集合个数， key [key ...] 指定进行计算的各个有序集合
	 */
	public void zunionstore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		/*System.out.println(cacheJedis.zadd("fruit-10-10", 300, "apple"));
		System.out.println(cacheJedis.zadd("fruit-10-10", 200, "banana"));
		System.out.println(cacheJedis.zadd("fruit-10-10", 150, "cherry"));
		System.out.println(cacheJedis.zadd("fruit-10-11", 250, "apple"));
		System.out.println(cacheJedis.zadd("fruit-10-11", 300, "banana"));
		System.out.println(cacheJedis.zadd("fruit-10-11", 100, "cherry"));
		System.out.println(cacheJedis.zunionstore("fruits-10-10&11","fruit-10-10","fruit-10-11"));*/
		System.out.println(cacheJedis.zrangeWithScores("fruits-10-10&11",0,-1));
	}
	
	@Test
	/**
	 * 16.交集
	 * zinterstore destkey numkeys key [key...],相同的元素的分值会被累加
	 * numkeys 参数指定要进行计算的有序集合个数， key [key ...] 指定进行计算的各个有序集合
	 */
	public void zinterstore(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		/*System.out.println(cacheJedis.zadd("fruit-10-10", 300, "apple"));
		System.out.println(cacheJedis.zadd("fruit-10-10", 200, "banana"));
		System.out.println(cacheJedis.zadd("fruit-10-10", 150, "cherry"));
		System.out.println(cacheJedis.zadd("fruit-10-11", 250, "apple"));
		System.out.println(cacheJedis.zadd("fruit-10-11", 300, "banana"));
		System.out.println(cacheJedis.zadd("fruit-10-11", 100, "cherry"));
		System.out.println(cacheJedis.zunionstore("fruits-10-10&11","fruit-10-10","fruit-10-11"));*/
		System.out.println(cacheJedis.zinterstore("fruits-10-10^11","fruit-10-10","fruit-10-11"));
		System.out.println(cacheJedis.zrangeWithScores("fruits-10-10^11",0,-1));
	}
	@Test
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
}