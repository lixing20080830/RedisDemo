package redis;

import redis.clients.jedis.Jedis;

public class TestRedis {
	public static void main(String[] args) {
		 //连接本地的 Redis 服务
	      Jedis jedis = new Jedis("10.200.11.153",6378);
	      //jedis.auth("123456");
	      System.out.println("Connection to server sucessfully");
	      //查看服务是否运行
	      System.out.println("Server is running: "+jedis.ping());
	      
	      /*jedis.set("foo", "bar");  
	      String value = jedis.get("foo");  
	      System.out.println(value);  */
	}
}
