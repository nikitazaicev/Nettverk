package no.hvl.dat110.messages;

public class SubscribeMsg extends Message {

	String topic;
	
	public SubscribeMsg() {
		super();
	}
	public SubscribeMsg(String user, String topic) {
		super(MessageType.SUBSCRIBE, user);
		this.topic = topic;
	}
		
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "SubscribeTopicMsg [topic=" + topic + "]";
	}
	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method

}
