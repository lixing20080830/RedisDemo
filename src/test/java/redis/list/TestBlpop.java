package redis.list;

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
public class TestBlpop {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	/*****************************推入和弹出操作(列表添加和删除)*********************************start***/
	@Test
	/**
	 * 1.从列表的左端推入值
	 * lpush key value [value ...]
	 * 将一个或以上数量的值依次推入到列表的左端，命令返回新 值被推入之后，列表目前包含的 项数量
	 * 复杂度为 O(n),n为推入的数量 
	 */
	public void lpush(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//从列表的左端推入值
		System.out.println(cacheJedis.lpush("list", "java"));
		System.out.println(cacheJedis.lpush("list", "c#"));
		System.out.println(cacheJedis.lpush("list", "c"));
		//从列表的左端推入多个值
		System.out.println(cacheJedis.lpush("list1", "1","2","3"));
	}
	
	@Test
	/**
	 * 2.从列表的右端推入值
	 * rpush key value [value ...]
	 * 将一个或以上数量的值依次推入到列表的右端，命令返回新 值被推入之后，列表目前包含的 项数量
	 * 复杂度为 O(n),n为推入的数量 
	 */
	public void rpush(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//从列表的右端推入值
//		System.out.println(cacheJedis.rpush("list2", "java"));
//		System.out.println(cacheJedis.rpush("list2", "c#"));
//		System.out.println(cacheJedis.rpush("list2", "c"));
//		//从列表的右端推入多个值
//		System.out.println(cacheJedis.rpush("list3", "1","2","3"));
		System.out.println(cacheJedis.rpush("lst", "1","2","3"));
	}
	
	@Test
	/**
	 * 3.从列表的两端弹出项
	 * lpop key 移除并返回列表最左端的 项,o(1)
	 * rpop key 移除并返回列表最右端的 项,o(1)
	 */
	public void lpop_rpop(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//从列表的右端推入值
		System.out.println(cacheJedis.lpop("list2"));
		System.out.println(cacheJedis.lpop("list3"));
		System.out.println(cacheJedis.rpop("list2"));
		System.out.println(cacheJedis.rpop("list3"));
	}
	/*****************************推入和弹出操作(列表添加和删除)*********************************end***/
	
	/*****************************阻塞式弹出操作*********************************start***/
	@Test
	/**
	 * 8.阻塞弹出
	 * blpop key [key...] timeout : LPOP 命令的阻塞版本；命令会以从左到右的顺序，访问给定的各个列表，并弹出首个非空列表最左端的项；
	 * 如果所有给定列表都为空，那么客户端将被阻塞，直到等待超时，或者有可弹出的项出现为止；设置 timeout 参数为 0 表示永远阻塞
	 * brpop key [key...] timeout : RPOP 命令的阻塞版本：命令会以从左到右的顺序，访问给定的各个列表，并弹出首个非空列表最右端的项；
	 * 如果所有给定列表都为空，那么客户端将被阻塞，直到等待超时，或者有可弹出的项出现为止；设置 timeout 参数为 0 表示永远阻塞
	 * 以上2个命令O(N)，N 为输入列表的数量
	 */
	public void blpop_brpop(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//System.out.println(cacheJedis.blpop(10,"empty-1","empty-2","empty-3"));
		//System.out.println(cacheJedis.rpush("lst", "one","two","three"));
		System.out.println(cacheJedis.blpop(0,"empty-1","empty-2","lst"));
	}
}