package redis.list.application.fixedFIFO;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.util.ObjectUtil;
import com.langqiao.util.RedisUtil;

public class FixedFIFO {
	
	private static Jedis jedis = null;
	
	//设置队列存储的哪个键
	private String key;
	//队列的最大长度
	private Long max_length;
	
	public FixedFIFO(String key,Long max_length){
		this.key = key;
		this.max_length = max_length;
		JedisPool pool = null;
		try {
			pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
			jedis = pool.getResource();
			max_length = jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//将给定值推入到队列中
	public void enqueue(Message message) throws Exception{
		//调用lpush命令来推入值
		Long list_length = jedis.lpush(key.getBytes(), ObjectUtil.objectToBytes(message));
		System.out.println(list_length);
		//调用ltrim命令来保持队列的最大长度
		System.out.println(jedis.ltrim(key, 0, max_length-1));
	}
	
	//弹出列表中最旧的值
	public Message dequeue() throws Exception{
		//调用rpop命令
		Message message = (Message)ObjectUtil.bytesToObject(jedis.rpop(key.getBytes()));
		return message;
	}
	
	//返回队列中包含的所有的值
	public void get_all_items(){
		//调用lrange命令
		List<byte[]> list = jedis.lrange(key.getBytes(), 0, -1);
		if(list.isEmpty()){
			System.out.println("队列为空");
			return;
		}
		for(byte[] bytes : list){
			try {
				Message message = (Message)ObjectUtil.bytesToObject(bytes);
				System.out.println("队列中数据为："+message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//获取队列的长度
	public long get_length(){
		return jedis.llen(key);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getMax_length() {
		return max_length;
	}

	public void setMax_length(Long max_length) {
		this.max_length = max_length;
	}
}
