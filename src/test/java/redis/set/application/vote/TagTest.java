package redis.set.application.vote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.langqiao.cache.RedisCacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class TagTest {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Test
	public void cast(){
		TagApi tag = new TagApi("set_tag_key::redis从入门到精通");
		System.out.println("打标签：" + tag.add("redis","国际米兰","AC米兰"));
		System.out.println("打标签：" + tag.add("大数据"));
		System.out.println("打标签：" + tag.add("高并发"));
		System.out.println("移除标签：" + tag.remove("redis","大数据"));
		System.out.println("标签是否存在：" +tag.include("redis"));
		System.out.println("标签是否存在：" +tag.include("高并发"));
		System.out.println("所有打的标签：" + tag.get_all());
		System.out.println("打标签的数量：" + tag.count());
	}
}
