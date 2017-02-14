package redis.set.application.vote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.langqiao.cache.RedisCacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-redis.xml")
public class VoteTest {
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Test
	public void cast(){
		VoteApi vote = new VoteApi("set_vote_key");
		System.out.println("投票：" + vote.cast("zhangsan"));
		System.out.println("投票：" + vote.cast("lisi"));
		System.out.println("投票：" + vote.cast("wangwu"));
		System.out.println("取消投票：" + vote.undo("zhangsan"));
		System.out.println("是否投票：" +vote.is_voted("zhangsan"));
		System.out.println("是否投票：" +vote.is_voted("lisi"));
		System.out.println("所有投票的人为：" + vote.members());
		System.out.println("总得投票人数为：" + vote.voted_count());
	}
}
