package no.hvl.dat110.messages;

public class PublishMsg extends Message {
	
	String user;
	String topic;
	String message;
	
	public PublishMsg() {
		super();
	}
	public PublishMsg(String user, String topic, String message) {
		this.user = user;
		this.topic = topic;
		this.message = message;
	}
		
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setTopic(String topic) {
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
	

	@Override
	public String toString() {
		return "SubscribeTopicMsg [user=" + user + ", topic=" + topic + ", message ="+ message+"]";
	}
	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method

}
