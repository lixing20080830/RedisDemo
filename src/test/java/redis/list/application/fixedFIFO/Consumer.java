package redis.list.application.fixedFIFO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.langqiao.cache.RedisCacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class Consumer {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	public static void main(String[] args) {
		FixedFIFO fixedFIFO = new FixedFIFO("fix_fifo_key", 10l);
		try {
			for(int i = 0;i < 10;i++){
				//从队列中弹出消息
				Message message = fixedFIFO.dequeue();
				System.out.println("出队列的结果为：" + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void dequeue() {
		FixedFIFO fixedFIFO = new FixedFIFO("fix_fifo_key", 10l);
		try {
			//for(int i = 0;i < 10;i++){
				//从队列中弹出消息
				Message message = fixedFIFO.dequeue();
				System.out.println("出队列的结果为：" + message);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
