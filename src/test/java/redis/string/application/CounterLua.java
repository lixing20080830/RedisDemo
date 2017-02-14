package redis.string.application;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.util.RedisUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class CounterLua {
	
    public static final String REDIS_LUA = "local count = redis.call('INCR',KEYS[1]);" +
    		"local re = '每分钟只能发送一次短信';" +
    		"if count == 1 then\n"+
    		"redis.call('EXPIRE',KEYS[1],ARGV[1]);" +
    		"return '短信发送成功..........';end;\n"+
    		"if count > tonumber(ARGV[2]) then\n"+
    		"return re;end;";
    private static final String redisScript;
	    
    static {
        redisScript = loadLuaScript();
    }
    
    private static String loadLuaScript() {
        Jedis jedis = null;
        String redisScript = null;
        JedisPool pool = null;
    	try {
    		pool = RedisUtil.getPool("127.0.0.1", 6379);
    		jedis = pool.getResource();
            redisScript = jedis.scriptLoad(REDIS_LUA);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
            	jedis.disconnect();
            }
        }
        return redisScript;
    }
	    
	public static void main(String[] args) {
		final List<String> keys = new ArrayList<String>();
		final List<String> argsarr = new ArrayList<String>();
		keys.add("count_test");
		argsarr.add("60");
		argsarr.add("1");
		//调用脚本
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						JedisPool pool = null;
				    	Jedis jedis = null;
				    	try {
				    		pool = RedisUtil.getPool("127.0.0.1", 6379);
				    		jedis = pool.getResource();
//				    		Object object = jedis.evalsha(redisScript, 3, "count_test","60","1");
				    		Object object = jedis.evalsha(redisScript,keys,argsarr);
				    		System.out.println(object.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}