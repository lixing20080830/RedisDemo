package redis.string;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.cache.RedisCache;
import com.langqiao.cache.RedisCacheManager;
import com.langqiao.util.RedisUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Counter1 {
	
	@Autowired
	private static RedisCacheManager cacheManager;
	
	@Autowired
	private static JedisConnectionFactory connectionFactory;
	
	
	private static Jedis cacheJedis;
	
    public static final String REDIS_LUA = "local key = KEYS[1];local expire = tonumber(KEYS[2]);" +
            "local number = tonumber(KEYS[3]);local count = tonumber(redis.call('GET',key));" +
            "if count == nil then redis.call('SETEX',KEYS[1],expire,'1');return 0;" +
            "else if count +1 >= number then redis.call('SETEX',KEYS[1],);return 0;" +
            "else redis.call('INCR',KEYS[1]);return 1;end;end;";
    
    private static final String redisScript;
	    
    static {
        redisScript = loadLuaScript();
        cacheJedis = ((RedisCache) cacheManager.getCache("default")).getCache();
    }
    
    private static String loadLuaScript() {
        Jedis jedis = null;
        String redisScript = null;
        try {
            redisScript = cacheJedis.scriptLoad(REDIS_LUA);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
            	cacheJedis.disconnect();
            }
        }
        return redisScript;
    }
	    
	    
	@Test
	public void test(){
		//调用脚本
		List<String> keys = new ArrayList<String>();
		List<String> args = new ArrayList<String>();
		JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
    		jedis = pool.getResource();
    		//jedis.evalsha(redisScript, 3, "lua_test_key", expireTime, countVal)
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object result = cacheJedis.evalsha(redisScript, keys,args);
	}
	
	
	@Test
	public void test1(){
		//调用脚本
		JedisConnection jedis = null;
		try {
			jedis = connectionFactory.getConnection();
			//jedis.setset("testtestkey", "hello world");
			//jedis.evalsha(redisScript, 3, "lua_test_key", expireTime, countVal)
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Object result = cacheJedis.evalsha(redisScript, keys,args);
	}
	
	
}
