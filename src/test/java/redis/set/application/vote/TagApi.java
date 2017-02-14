package redis.set.application.vote;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.util.RedisUtil;

public class TagApi {
	
	private static Jedis jedis = null;
	//使用集合存储所打的标签
	private String key;
	
	public TagApi(String key){
		this.key = key;
		JedisPool pool = null;
		try {
			pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
			jedis = pool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//添加标签
	public Long add(String... tag){
		//使用sadd将用户添加到集合里面
		return jedis.sadd(key, tag);
	}
	
	//移除所打标签
	public Long remove(String... tag){
		return jedis.srem(key, tag);
	}
	
	//检查某个标签是否存在
	public boolean include(String tag){
		return jedis.sismember(key, tag);
	}
	
	//返回所有标签
	public Set<String> get_all(){
		return jedis.smembers(key);
	}
	
	//返回所有标签的数量
	public Long count(){
		return jedis.scard(key); 
	}
}
