package com.langqiao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author mingyangyang
 * 2016.06.14
 */
public class RedisAPI {
	
	private static JedisPool pool =  null;
	
	public static void main(String[] args) {
		/*System.out.println(set("test-key", "new-test-value"));
		System.out.println(get("test-key"));*/
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						counter();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		System.out.println("test_counter的最后的值为："+get("test_countervbbbbbbddddddddddddddddd"));
	}
	/**
	 * 构建redis连接池
	 * @param host
	 * @param port
	 * @return JedisPool
	 */
	public static JedisPool getPool(String host,int port){
		if(pool == null){
			JedisPoolConfig config = new JedisPoolConfig();
			//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(500);
			//config.setMaxActive(500);
			//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(100*100);
			//config.setMaxWait(100*1000);
			 //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, host, port);
		}
		return pool;
	}
	
	/**
	 * 构建redis连接池
	 * @param host
	 * @param port
	 * @return JedisPool
	 */
	public static JedisPool getPool(String host,int port,String password){
		if(pool == null){
			JedisPoolConfig config = new JedisPoolConfig();
			//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(-1);
			//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(100*1000);
			//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, host, port, 10000000, password);
		}
		return pool;
	}
	
	 /**
     * 返还到连接池
     * @param pool 
     * @param redis
     */
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
    
    /**
     * 设置redis 数据
     * @param key
     * @return 
     * @return
     */
    public static String set(String key,String value){
    	String result = null;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getPool("106.12.37.42", 6379);
    		jedis = pool.getResource();
    		result = jedis.set(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally{
			returnResource(pool, jedis);
		}
    	return result;
    }
    
    /**
     * 设置redis 数据
     * @param key
     * @return 
     * @return
     */
    public static long setnx(String key,String value){
    	long result = 0;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getPool("106.12.37.42", 6379);
    		jedis = pool.getResource();
    		result = jedis.setnx(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally{
			returnResource(pool, jedis);
		}
    	return result;
    }
    
    /**
     * 获取redis 数据
     * @param key
     * @return
     */
    public static String get(String key){
    	String value = null;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		//pool = getPool("localhost", 6379);
    		pool = RedisAPI.getPool("106.12.37.42", 6379,"lixing");
    		jedis = pool.getResource();
    		value = jedis.get(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally{
			returnResource(pool, jedis);
		}
    	return value;
    }
    
    /**
     * 计数器API及其实现
     */
    public static void counter(){
    	String result = null;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		//pool = getPool("localhost", 6379);
    		pool = RedisAPI.getPool("106.12.37.42", 6379,"lixing");
    		jedis = pool.getResource();
    		//long counter = jedis.incr("msg2");
    		System.out.println(jedis.incr("特朗普，波尔吉尼斯，波多野结衣"));
    		//System.out.println(Thread.currentThread().getName());
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally{
			returnResource(pool, jedis);
		}
    }
}
