package no.hvl.dat110.messages;

public class DeleteTopicMsg extends Message {
	
	String user;
	String topic;
	
	public DeleteTopicMsg() {
		super();
	}
	public DeleteTopicMsg(String user, String topic) {
		this.user = user;
		this.topic = topic;
	}
		
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "SubscribeTopicMsg [user=" + user + ", topic=" + topic + "]";
	}
	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method

}
