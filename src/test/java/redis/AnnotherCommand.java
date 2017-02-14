package redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * @author mingyangyang
 * 1.散列(hash),一个散列由多个域值对（field-value pair）组成，
 * 2.散列的域和值都可以是文字、整数、浮点数或者二 进制数据,同一个散列里面的每个域必 须是独一无二、各不相同
 * 的，而域的值则没有这一要求，换句话说，不同域的值可以是重复的
 * 3.通过命令，用户可以对散列执行设置域值对、获取域的值、检查域是否存在等操作，也可以 让 Redis 返回散列包含的所有域、所有 值或者所有域值对
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class AnnotherCommand {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	/*****************************基本操作*********************************start***/
	@Test
	/**
	 * 1.查看键的类型
	 * type key
	 * 复杂度为 O(1) 
	 * 返回键key存储的值的类型
	 * none:键不存在
	 * string:字符窜或者hyperloglog
	 * hash:散列
	 * list:列表
	 * set:集合
	 * zset:有序集合
	 */
	public void type(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field没有关联值，命令返回1，如果有，命令新值覆盖旧值，返回0
		System.out.println(cacheJedis.type("number3"));
	}
	
	@Test
	/**
	 * 2.删除键
	 * del key [key...]
	 * 删除多个键，键不存在则忽略，命令返回成功删除的键的数量
	 * 复杂度为 O(n) n为被删除的键的数量
	 */
	public void del(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field没有关联值，命令返回1，如果有，命令新值覆盖旧值，返回0
		System.out.println(cacheJedis.del("num111"));
	}
	
	@Test
	/**
	 * 3.判断键是否存在
	 * exists key,存在返回为1，不存在返回0
	 * 复杂度为 O(1) 
	 */
	public void exists(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.exists("number3"));
		System.out.println(cacheJedis.get("number3"));
	}
	
	@Test
	/**
	 * 4.修改键的名字
	 * rename key newkey 将键的名字由key改为newkey,如果newkey存在则覆盖，键key不存在或者key与newkey同名时返回错误，成功OK
	 * 复杂度为 O(1) 
	 */
	public void rename(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field没有关联值，命令返回1，如果有，命令新值覆盖旧值，返回0
		System.out.println(cacheJedis.rename("number3","number33"));
	}
	
	@Test
	/**
	 * 5.修改键的名字
	 * rename key newkey 如果newkey不存在将key改为newkey，如果键key存在不做任何动作，成功1，失败0
	 * 复杂度为 O(1) 
	 */
	public void renamenx(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//如果域field没有关联值，命令返回1，如果有，命令新值覆盖旧值，返回0
		System.out.println(cacheJedis.renamenx("number1","number33"));
	}
	
	@Test
	/**
	 * 6.排序
	 * sort key
	 * 复杂度为 O(1) 
	 */
	public void sort(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//System.out.println(cacheJedis.rpush("num111", "70","3","10","1"));
		//按照本身大小排序
		System.out.println(cacheJedis.sort("num111"));
		System.out.println(cacheJedis.sort("num111", new SortingParams().desc()));
		System.out.println(cacheJedis.zadd("scores", 4, "peter"));
		System.out.println(cacheJedis.zadd("scores", 5, "jack"));
		System.out.println(cacheJedis.zadd("scores", 6, "tom"));
		System.out.println(cacheJedis.zrange("scores", 0, -1));
		//文字排序，根据alpha参数对元素本身来进行排序，字母顺序
		System.out.println(cacheJedis.sort("scores", new SortingParams().alpha()));
	}
	
	@Test
	/**
	 * 7.随机获取某个键
	 * randomkey key
	 * 随机返回一个键，并且不会被删除
	 * 如果键不存在，返回nil
	 * 复杂度为 O(1) 
	 */
	public void randomkey(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.randomKey());
	}
	
	@Test
	/**
	 * 8.返回数据库中与给定模式匹配的键
	 * keys pattern
	 * pattern 参数的值可以是任何glob风格的匹配符
	 *  glob 模式是指 shell 所使用的简化了的正则表达式。
	 *  星号（*）匹配零个或多个任意字符；[abc]匹配任何一个列在方括号中的字符（这个例子要么匹配一个 a，要么匹配一个 b，要么匹配一个 c）；
	 *  问号（?）只匹配一个任意字符；如果在方括号中使用短划线分隔两个字符，表示所有在这两个字符范围内的都可以匹配（比如 [0-9] 表示匹配所有 0 到 9 的数字）
	 * keys *  数据库中的所有键
	 * keys h?llo  hello、hallo、hxllo
	 * keys h*llo  hello、hllo、heeeeeello
	 * keys h[ae]llo 匹配hello和hallo,不匹配hillo
	 * 复杂度为 O(n),n为数据库包含的键值对数量 
	 */
	public void keyspattern(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//redis数据量比较大时，keys * 会遍历整个数据库，导致服务阻塞一段时间
		//redis从2.8.0开始提供scan命令，以渐进方式，分多次遍历整个数据库，并返回给定匹配模式的键
		System.out.println(cacheJedis.keys("*"));
	}
	
	@Test
	/**
	 * 9.返回数据库中与给定模式匹配的键
	 * scan 命令用法
	 * scan cursor [match pattern] [count number]
	 * cursor是遍历时的游标，第一遍历时需将cursor设置为0,之后每次调用scan会返回一个新的游标值,因为在此调用scan需要这个游标值，当游标为0时，遍历结束
	 * match pattern 指定匹配的模式，类似keys的pattern参数
	 * count number 遍历最多要返回多少个键,number默认值为10
	 * 该命令可能返回一个键多次，需要在客户端做过滤
	 */
	public void scan(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//redis数据量比较大时，keys * 会遍历整个数据库，导致服务阻塞一段时间
		//redis从2.8.0开始提供scan命令，以渐进方式，分多次遍历整个数据库，并返回给定匹配模式的键
	}
	
	/*****************************处理数据库本身*********************************start***/
	@Test
	/**
	 * 10.获取数据库大小
	 * dbsize 返回数据库中包含的键值对的数量
	 * 复杂度为o(1)
	 */
	public void dbsize(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.dbSize());
	}
	
	
	@Test
	/**
	 * 11.清空当前数据库
	 * flushdb 删除当前数据库包含的所有键值对，删除成功返回OK
	 * 复杂度为o(n) n为被删除的键值对的数量
	 */
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
	
	@Test
	/**
	 * 12.清空所有数据库
	 * flushall 
	 * 复杂度为o(n) n为所有键值对的数量
	 */
	public void fluashall(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushAll();
		System.out.println("缓存清空成功........");
	}
	
	/**
	 * 14.切换数据库
	 * redis 默认创建16个数据库，0~15
	 * 不同数据库里名字相同的键不会造成冲突
	 * select num
	 */
	
	/**
	 * 15.在数据库之间移动键值对
	 * move key target-db
	 * 将当前数据库的键移动到目标数据库target-db里面，成功返回1，失败0
	 * 如果target-db已经有名为key的键或者键key不存在于当前数据库，则不做任何动作
	 * 复杂度o(1)
	 */
}