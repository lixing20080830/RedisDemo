package redis.list.application.messageQueue;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.langqiao.cache.RedisCacheManager;
import com.langqiao.util.ObjectUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Consumer {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Test
	public void dequeue() {
		MessageQueue messageQueue = new MessageQueue("message_queue");
		try {
			List<byte[]> bytes = messageQueue.dequeue(0);
			if(bytes.isEmpty()){
				System.out.println("队列目前为空！");
			}else{
				Message message = (Message)ObjectUtil.bytesToObject(bytes.get(0));
				System.out.println("出队列的数据为:"+message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MessageQueue messageQueue = new MessageQueue("message_queue");
		try {
			List<String> bytes = messageQueue.dequeue_str(0);
			if(bytes.isEmpty()){
				System.out.println("队列目前为空！");
			}else{
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes.get(0).getBytes());
				ObjectInputStream ois = new ObjectInputStream(bis);
				Message message = (Message)ois.readObject();
				System.out.println("出队列的数据为:"+message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
