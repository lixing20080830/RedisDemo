package redis.string;

import java.util.BitSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;

/**
 * 
 * @author mingyangyang
 * 不要使用 STRLEN、SETRANGE 和 GETRANGE 来处理中文。这几个命令都是基于字节而不是字符的
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class BasicCommand {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Test
	/**
	 * 1.为字符串键设置值
	 * set msg "hello world"
	 * set msg "goodbye"
	 * 复杂度为 O(1) 
	 */
	public void set(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//将字符串键 key 的值设置为 value ，命令返回 OK 表示设置成功
		System.out.println(cacheJedis.set("msg", "hello world"));
		System.out.println(cacheJedis.get("msg"));
		//如果字符串键 key 已经存在，那么用新值覆盖原来的旧值。
		cacheJedis.set("msg", "goodbye");
		System.out.println(cacheJedis.get("msg"));
		
	}
	
	
	@Test
	/**
	 * 2.
	 * SET key value [NX],not exsist(不存在)
	 * 仅在键 key 不存在的情况下，才进行设置操作；如果键 key 已经存在，那么 SET ... NX 命令不做动作（不会覆盖旧值）
	 * SET msg_nx "this opt success" NX
	 * 复杂度为 O(1) 
	 */
	public void set_NX(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//键不存在，所以指定 NX 选项是可行的,OK：成功，nil:失败
		System.out.println(cacheJedis.set("msg_nx", "msg_nx opt success","nx"));
		//键已经存在，指定 NX 选项导致设置失败
		System.out.println(cacheJedis.set("msg_nx", "msg_nx opt fail","nx"));
		System.out.println(cacheJedis.get("msg_nx"));
	}
	
	@Test
	/**
	 * 3.
	 * SET key value [XX],exsist(存在)
	 * 仅在键 key 已经存在的情况下，才进行设置操作（一定会覆盖旧值）；如果键 key 不存在，那么 SET ... XX 命令不做动作。
	 * SET msg_xx "this opt success" XX
	 * 复杂度为 O(1) 
	 */
	public void set_XX(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//键不存在，所以指定 XX 选项导致失败,OK：成功，nil:失败
		System.out.println(cacheJedis.set("msg_xx", "msg_xx opt fail","xx"));
		//键不存在，所以指定 NX 选项是可行的
		System.out.println(cacheJedis.set("msg_xx", "msg_xx opt success","nx"));
		//键已经存在，指定 XX 选项导致设置成功
		System.out.println(cacheJedis.set("msg_xx", "msg_xx opt success again","xx"));
		System.out.println(cacheJedis.get("msg_xx"));
	}
	
	@Test
	/**
	 * 4.仅在键不存在的情况下进行设置
	 * SETNX key value
	 * 仅在键 key 不存在的情况下，将键 key 的值设置为 value ，效果和 SET key value NX 一样。NX 的意思为“Not eXists”（不存在）
	 * 键不存在并且设置成功时，命令返回 1 ；因为键已经存在而导致设置失败时，命令返回 0 。
	 * SETNX nxMsg "this opt success"
	 * 复杂度为 O(1) 
	 */
	public void setNX(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		//键不存在，所以指定 NX 选项是可行的,1:成功，0：失败，原始命令(OK：成功，nil:失败)
		System.out.println(cacheJedis.setnx("nxMsg", "this opt success"));
		//键已经存在，指定 NX 选项导致设置失败
		System.out.println(cacheJedis.setnx("nxMsg", "this opt fail"));
		System.out.println(cacheJedis.get("nxMsg"));
	}
	
	@Test
	/**
	 * 5.同时设置或获取多个字符串键的值
	 * 一次为一个或多个字符串键设置值，效果和同时执行多个 SET命令一样。
	 * MSET key value [key value ...](m->more),成功:OK,复杂度为 O(N)，N为要设置的字符窜键的数量 
	 * MGET key [key ...],成功:OK,复杂度为 O(N)，N为要获取的字符窜键的数量 
	 */
	public void mset_mget(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.mset("zhangsan::email", "zhangsan@pptv.com","zhangsan::company","pptv","zhangsan::home","china","zhangsan::school","qinghua"));
		System.out.println(cacheJedis.mget("zhangsan::email","zhangsan::company","zhangsan::home","zhangsan::school"));
	}
	
	@Test
	/**
	 * 6.一次设置多个不存在的键
	 * 只有在所有给定键都不存在的情况下， MSETNX 会为所有给定键设置值，效果和同时执行多个SETNX 一样。如果给定的键至少有一个是存在的，那么 MSETNX 将不执行任何设置操作
	 * 返回 1 表示设置成功，返回 0 表示设置失败。复杂度为 O(N) ， N 为给定的键数量
	 * MSETNX nx-1 "hello" nx-2 "world" nx-3 "good luck"
	 */
	public void msetnx(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.msetnx("nx-1","1111","nx-2","2222","nx-3","3333"));
		System.out.println(cacheJedis.set("ex-key", "bad key here"));
		System.out.println(cacheJedis.msetnx("nx-4","4444","nx-5","5555","ex-key","6666"));
		System.out.println(cacheJedis.mget("nx-1","nx-2","nx-3","nx-4","nx-5","ex-key"));
	}
	
	@Test
	/**
	 * 7.设置新值，返回旧值
	 * 将字符串键的值设置为 new-value ，并返回字符串键在设置新值之前储存的旧值（old value）
	 * 复杂度为 O(1)
	 * GETSET getset-str "new value"
	 */
	public void getset(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("getset-str","old value"));
		System.out.println(cacheJedis.getSet("getset-str", "new value"));
		System.out.println(cacheJedis.get("getset-str"));
	}
	
	@Test
	/**
	 * 8.追加内容到字符窜末尾
	 * 将值 value 推入到字符串键 key 已储存内容的末尾
	 * 复杂度为 O(N)， 其中 N 为被推入值的长度
	 * APPEND cellphone "-11001"
	 */
	public void appendStr(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("cellphone","iphone7s"));
		System.out.println(cacheJedis.append("cellphone", "-11001"));
		System.out.println(cacheJedis.get("cellphone"));
	}
	
	@Test
	/**
	 * 9.返回值的长度
	 * 一个英文字符只需要使用 单个字节来储存，而一个中文字符却需要使用多个字 节来储存
	 * GBK\GB2312编码是2个字节，但是(unicode/utf-8)等就不同了，这些编码里面汉字是长度不是一定的，有的长、有的短，2～4字节 
	 * STRLEN、SETRANGE 和 GETRANGE 都是为英文设置的，它们只会在字符为单个字节的情况下正常工作，而一旦我们储存的是类似中文这样的多字节字符，那么这三个命令就不再适用了
	 * 不要使用 STRLEN、SETRANGE 和 GETRANGE 来处理中文。
	 * 如果你想知道被 储存的中文包含多少个字节，那么可以使用 STRLEN
	 * 返回字符串键 key 储存的值的长度
	 * 复杂度为 O(1)，  Redis 会记录每个字符串值的长度
	 * STRLEN cellphone
	 */
	public void strlen(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("cellphone","世界你好"));
		System.out.println(cacheJedis.strlen("cellphone"));
	}
	
	@Test
	/**
	 * 10.范围设置
	 * SETRANGE key index value
	 * 从索引 index 开始，用 value 覆写（overwrite）给定键 key 所储存的字符串值。只接受正数索引
	 * 命令返回覆写之后，字符串 值的长
	 * 复杂度为 O(N)， N 为 value 的长度
	 * STRLEN cellphone
	 */
	public void setrange(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("setrange","hello"));
		System.out.println(cacheJedis.setrange("setrange", 1, "appy"));
		System.out.println(cacheJedis.get("setrange"));
	}
	
	@Test
	/**
	 * 11.范围取值
	 * SETRANGE key index value
	 * 返回键 key 储存的字符串值中，位于 start 和 end 两个索引之间的内容（闭区间，start 和 end 会被包括在内）,索引值可为正，可为负
	 * 复杂度为 O(N) ， N 为被选中内容的长度。
	 *  GETRANGE msg 0 4
	 */
	public void getrange(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("getrange","hello"));
		System.out.println(cacheJedis.getrange("getrange",0,-1));
	}
	
	/***************************数字操作**********************************************start*/
	@Test
	/**
	 * 12.增加或者减少数字的值
	 * INCRBY key increment,将 key 所储存的值加上增量 increment,命令返回操作执行之后，键 key 的当前值,O(1)
	 * DECRBY key decrement,将 key 所储存的值减去减量 decrement,命令返回操作执行之后，键 key 的当前值,O(1)
	 * 如果执行 INCRBY 或者 DECRBY 时，键 key 不存在，那么命令会将 键 key的值初始化为 0 ，然后再执行增加或者减少操作
	 */
	public void numberopt_by(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.incrBy("numm1", 100));
		System.out.println(cacheJedis.incrBy("numm1", 20));
		System.out.println(cacheJedis.decrBy("numm1", 10));
	}
	
	@Test
	/**
	 * 13.增一和减一
	 * INCR key == INCRBY key 1,O(1)
	 * DECR key == DECRBY key 1,O(1)
	 * 如果执行 INCRBY 或者 DECRBY 时，键 key 不存在，那么命令会将 键 key的值初始化为 0 ，然后再执行增加或者减少操作
	 */
	public void numberopt(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("numberopt", "100"));
		System.out.println(cacheJedis.incr("numberopt"));
		System.out.println(cacheJedis.decr("numberopt"));
	}
	
	@Test
	/**
	 * 14.浮点数的自增和自减
	 * INCRBYFLOAT key increment
	 * 如果执行 INCRBY 或者 DECRBY 时，键 key 不存在，那么命令会将 键 key的值初始化为 0 ，然后再执行增加或者减少操作
	 */
	public void floatIncr(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache(); 
		System.out.println(cacheJedis.set("floatIncr", "10"));
		//通过传递负值来达到做减法的效果,没有相应的DECRBYFLOAT
		System.out.println(cacheJedis.incrByFloat("floatIncr", -3.14));
	}
	/***************************数字操作**********************************************end*/
	
	
	/***************************二进制数据操作******************************************start*/
	@Test
	/**
	 * 15.设置和获取二进制数据
	 * setbit key index value 给定索引上的二进制的值设置为value,命令返回被设置的位原来存储的旧值，复杂度o(1)
	 * getbit key index 返回给定索引上的二进制位的值,复杂度为 O(1) 。
	 * BITCOUNT key [start] [end] 计算并返回字符串键储存的值中，被设置为 1 的二进制位的数量
	 * 一般情况下，给定的整个字符串键都会进行计数操作，但通过指定额外的 start 或 end 参数，可以让计数只在特定索引范围的位上进行
	 * start 和 end 参数的设置和 GETRANGE 命令类似，都可以使用负数值：比如 -1 表示最后一个位，而 -2表示倒数第二个位，以此 类推,复杂度为 O(N) ，其中 N 为被计算二进制位的数量
	 * bitcount源码所在，G:\github_git\redis-3.0-annotated\src\bitops.c，550line, 
	 * if (start < 0) start = strlen+start;
     * if (end < 0) end = strlen+end;
     * if (start < 0) start = 0;
     * if (end < 0) end = 0;
     * if (end >= strlen) end = strlen-1;
	 */
	public void redisBinary(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		BinaryJedis binaryJedis = (BinaryJedis) redisCache.getCache(); 
		//经过测试，下面方法返回的是上一次的结果
		/*System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 0, true));
		System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 1, true));
		//System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 2147483647, false));
		System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 4, false));
		System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 2, false));
		System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 3, false));*/
		//System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 5, true));
		System.out.println(binaryJedis.setbit("redis_binary4".getBytes(), 8, true));
		System.out.println("二进制数中的个数为："+binaryJedis.bitcount("redis_binary4".getBytes(), 1, 5));
		System.out.println("二进制数中的个数为："+binaryJedis.bitcount("redis_binary4".getBytes(), -4, 2));
		System.out.println(binaryJedis.get("redis_binary4".getBytes()));
		BitSet users = BitSet.valueOf(binaryJedis.get("redis_binary4".getBytes())); 
		//System.out.println(users.cardinality());
	}
	
	@Test
	/**
	 * 16.二进制位运算
	 * bitop and destkey key [key...] 对一个或者多个key求逻辑并(a和b都为true，才为true)，并将结果保存到destkey
	 * bitop or destkey key [key...] 对一个或者多个key求逻辑或(如果一个操作数或多个操作数为 true，则逻辑或运算符返回布尔值 true；只有全部操作数为false，结果才是 false)，并将结果保存到destkey
	 * bitop xor destkey key [key...] 对一个或者多个key求逻辑异或(如果a、b两个值不相同，则异或结果为1。如果a、b两个值相同，异或结果为0。)，并将结果保存到destkey
	 * bitop not destkey key 对给定key求逻辑非(逻辑非，就是指本来值的反值)，并将结果保存到destkey
	 * 复杂度为 O(N) ， N 为进行计算的二进制位数量的总和。
	 * 命令的返回值为计算所得结果的字节长度，相当于对 destkey 执行 STRLEN
	 */
	public void bitop(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		BinaryJedis binaryJedis = (BinaryJedis) redisCache.getCache(); 
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 0, true));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 1*8, false));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 2*8, true));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 3*8, true));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 4*8, false));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 5*8, false));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 6*8, true));
//		System.out.println(binaryJedis.setbit("b1".getBytes(), 7*8, false));
		System.out.println(binaryJedis.setbit("b1".getBytes(), 8*8, true));
//		
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 0, true));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 1*8, false));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 2*8, true));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 3*8, false));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 4*8, true));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 5*8, true));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 6*8, false));
//		System.out.println(binaryJedis.setbit("b2".getBytes(), 7*8, true));
		
		//jedis包装的api中返回的是位bit的长度
		System.out.println(binaryJedis.bitop(BitOP.AND, "b1-and-b2".getBytes(), "b1".getBytes(),"b2".getBytes()));
	}
	
	private String StrToBinstr(String str) {
		char[] strChar=str.toCharArray();
		String result="";
		for(int i=0;i<strChar.length;i++){
			result +=Integer.toBinaryString(strChar[i])+ " ";
		}
		return result;
	}
	
	/***************************二进制数据操作******************************************end*/
	
	@Test
	public void fluashDb(){
		RedisCache redisCache = (RedisCache) cacheManager.getCache("default");
		Jedis cacheJedis = redisCache.getCache();
		cacheJedis.flushDB();
		System.out.println("缓存清空成功........");
	}
	public static void main(String[] args) {
		/*String redisKey = "SMS_LIMIT_" + smsPhone;
		long count = redisTemplate.opsForValue().increment(redisKey, 1);
		if (count == 1) {
		//设置有效期一分钟
		redisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);
		}
		if (count > 1) {
		resultMap.put("retCode", "-1");     resultMap.put("retMsg", "每分钟只能发送一次短信");
		outPrintJson(resultMap);
		return;
		}*/
	}
}
