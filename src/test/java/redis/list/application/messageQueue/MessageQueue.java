package redis.list.application.messageQueue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.langqiao.util.ObjectUtil;
import com.langqiao.util.RedisUtil;

public class MessageQueue {
	
	private static Jedis jedis = null;
	
	//设置队列存储的哪个键
	private String key;
	
	public MessageQueue(String key){
		this.key = key;
		JedisPool pool = null;
		try {
			pool = RedisUtil.getPool("10.200.11.153", 6379,"123456");
			jedis = pool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//将给定值推入到队列中
	public void enqueue(Message message) throws Exception{
		//调用lpush命令来推入值
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		String msg1 = baos.toString();
		
		Long list_length = jedis.lpush(key, msg1);
		System.out.println(list_length);
	}
	
	//在给定的时限内，等待队列弹出列表中最旧的项
	public List<byte[]> dequeue(int timeout) throws Exception{
		//调用brpop命令
		List<byte[]> bytes = jedis.brpop(timeout, key.getBytes());
		if(bytes == null){
			bytes = new ArrayList<byte[]>();
		}
		return bytes;
	}
	
	//在给定的时限内，等待队列弹出列表中最旧的项
	public List<String> dequeue_str(int timeout) throws Exception{
		//调用brpop命令
		List<String> bytes = jedis.brpop(timeout, key);
		return bytes;
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
}
