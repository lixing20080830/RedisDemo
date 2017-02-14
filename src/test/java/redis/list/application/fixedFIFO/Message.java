package redis.list.application.fixedFIFO;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = -6031587398200562791L;
	//id
	private Integer id;
	//内容
	private String content;
	
	public Message(Integer id,String content){
		this.id = id;
		this.content = content;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", content=" + content + "]";
	}
	
}
