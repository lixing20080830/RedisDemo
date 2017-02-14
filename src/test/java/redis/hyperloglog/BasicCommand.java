package redis.hyperloglog;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * @author mingyangyang
 * 记录网站每天获得的独立 IP 数量,为了很好解决独立IP地址计算问题Redis 在 2.8.9 版本添加了 HyperLogLog 结构
 * HyperLogLog 可以接受多个元素作为输入，并给出输入元素的基数估算值
 * HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以HyperLogLog 不能像集合那样，返回输入的各个元素
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class BasicCommand {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Test
	/**
	 * 1.将元素添加至hyperloglog
	 * pfadd key element[element...]
	 * 将任意数量的元素添加到指定的 HyperLogLog 里面
	 * 这个命令可能会对 HyperLogLog 进行修改，以便反映新的基数估算 值，如果 HyperLogLog 的基数估算值在命令执行之后出现了变化， 那么命令返回 1 ， 否则返回 0
	 * 命令的复杂度为 O(N) ，N 为被添加元素的数量
	 */
	public void pfadd(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//cacheJedis.pfadd(key, elements)
	}
	
	@Test
	/**
	 * 2.返回给定 HyperLogLog 的基数估算值
	 * pfcount key [key...]
	 * 当给定多个 HyperLogLog 时，命令会先对给定的 HyperLogLog 进行并集计算，得出一个合并后的HyperLogLog ，
	 * 然后返回这个合并 HyperLogLog 的基数估算值作为命令的结果（合并得出的HyperLogLog 不会被储存，使用之后就会被删掉）
	 * 当命令作用于单个 HyperLogLog 时， 复杂度为 O(1) 当命令作用于多个 HyperLogLog 时， 复杂度为 O(N) 
	 */
	public void pfcount(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
	}
	
	@Test
	/**
	 * 3.合并多个 HyperLogLog
	 * pfmerge destkey sourcekey[sourcekey...]
	 * 将多个 HyperLogLog 合并为一个 HyperLogLog ，合并后的 HyperLogLog 的基数估算值是通过对所有给定 HyperLogLog 进行并集计算得出的
	 * 命令的复杂度为 O(N) ， 其中 N 为被合并的 HyperLogLog 数量
	 */
	public void pfmerge(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
	}
	
	@Test
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
}