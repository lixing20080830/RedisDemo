package redis.set.application.vote;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.util.RedisUtil;

public class VoteApi {
	
	private static Jedis jedis = null;
	//使用集合存储所有已投票的人
	private String key;
	
	public VoteApi(String key){
		this.key = key;
		JedisPool pool = null;
		try {
			pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
			jedis = pool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//投票
	public Long cast(String user){
		//使用sadd将用户添加到集合里面
		return jedis.sadd(key, user);
	}
	
	//取消投票
	public Long undo(String user){
		return jedis.srem(key, user);
	}
	
	//检查用户是否已投票
	public boolean is_voted(String user){
		return jedis.sismember(key, user);
	}
	
	//返回所有已投票的用户
	public Set<String> members(){
		return jedis.smembers(key);
	}
	
	//返回所有投票的用户数量
	public Long voted_count(){
		return jedis.scard(key); 
	}
}
