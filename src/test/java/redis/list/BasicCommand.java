package redis.list;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * @author mingyangyang
 * 1.列表(list),一个列表可以包含一个或以上数量的 项（item），每个项按照它们被推入到列表的位置来排列
 * 2.每个列表项所处的位置决定了这个项的索引值（index），索引以 0 为开始，从列表的左端到右端依次 递
 * 增，位于列表最左端（表 头）的项的索引为 0 ，而位于列表最右端（表尾）的 项的索引为 N-1 ，其中 N 为列表的长度
 * 3.列表包含的项可以出现重复，它们不必是唯一的
 * 通过使用 Redis 列表键，我们也可以构建一个这样的定长先进先出队列。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class BasicCommand {
	
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
	
	/*****************************长度、索引和范围操作*********************************start***/
	@Test
	/**
	 * 4.获取列表的长度
	 * llen key 返回列表长度，列表项数量，复杂度o(1),Redis 会记录每个列表的长度，所以这个命令无须遍历列表
	 * lindex key index 返回给定索引上的项,index索引可以为正数也可以为负数，正向索引从0(从左到右)开始，负向索引从-1(从右向左)开始
	 * lrange key start stop,返回键key中从索引start到stop范围内的所有项，可以为正数和负数，o(n),n为返回的数量
	 */
	public void llen_lindex_lrange(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.llen("list2"));
		System.out.println(cacheJedis.lindex("list2",-1));
		System.out.println(cacheJedis.lrange("list2",0,-1));
		//从列表的右端推入值
	}
	/*****************************长度、索引和范围操作*********************************end***/
	
	/*****************************插入和删除操作*********************************start***/
	@Test
	/**
	 * 5.设置指定索引项的值
	 * lset key index value 将列表键 key 索引 index 上的列表项设置为value ，设置成功时命令返回 OK 
	 * 如果 index 参数超过了列表的索引范围，那么命令返回一个错误
	 * 表头和表尾节点进行处理时（index 为 0 或者 -1），命令的复杂度为 O(1) ；其他情况下，命令的复杂度为 O(N) ，N 为列表的长度
	 */
	public void lset(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.lset("list2",0,"test2222222222"));
		System.out.println(cacheJedis.lindex("list2",-1));
	}
	
	
	@Test
	/**
	 * 6.在指定位置插入列表项
	 * linsert key before|after item value 根据命令调用时传递的是 BEFORE 选项还是 AFTER 选项，将值 value 插入到指定列表项 pivot 的之前
	 * 或者之后。当 pivot 不存在于列表 key 时，不执行任何操作
	 * 返回 -1 表示 pivot 不存在；返回 0 表示键 key 不存在；插入成功时则返回列表当前的长度
	 * 复杂度为 O(N) ，N 为列表长度
	 */
	public void linsert(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.linsert("list2", LIST_POSITION.AFTER, "test2222222222", "test33333333"));
		System.out.println(cacheJedis.linsert("list2", LIST_POSITION.BEFORE, "test2222222222", "test1111111"));
		System.out.println(cacheJedis.lrange("list2",0,-1));
		//从列表的右端推入值
	}
	
	@Test
	/**
	 * 7.从列表中删除指定值
	 * lrem key count value 根据参数count的值，移除列表中与参数value相等的列表项
	 * 命令返回被移除列表项的数量
	 * 命令的复杂度为 O(N) ，N 为列表的长度
	 * 如果 count > 0 ，那么从表头开始向表尾搜索，移除最多 count 个值为 value 的列表项。
	 * 如果 count < 0 ，那么从表尾开始向表 头搜索，移除最多 abs(count) 个值为 value 的列表项。
	 * 如果 count = 0 ，那么移除列表中所有 值为 value 的列表项。
	 */
	public void lrem(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.lrem("list2", 0, "test33333333"));
		System.out.println(cacheJedis.lrange("list2",0,-1));
	}
	
	@Test
	/**
	 * 8.修剪列表
	 * ltrim key start stop
	 * 对一个列表进行修剪(trim)，让列表只保留指定索引范 围内的列表项，而将不在范围内的其他列表项全部删除。两个索引都可以是正数或者负数
	 * 命令执行成功时返回 OK ，复杂度为 O(N) ，N 为被移除列表项的数量
	 */
	public void ltrim(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.ltrim("list2", 0, -1));
		System.out.println(cacheJedis.lrange("list2",0,-1));
	}
		/*****************************插入和删除操作*********************************end***/
		
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
	/*****************************阻塞式弹出操作*********************************end***/
	@Test
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
}