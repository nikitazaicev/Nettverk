package no.hvl.dat110.messages;

public class CreateTopicMsg extends Message {
	
	String topic;
	
	public CreateTopicMsg() {
		super();
	}
	public CreateTopicMsg(String user, String topic) {
		super(MessageType.CREATETOPIC, user);
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
		return "CreateTopicMsg [topic=" + topic + "]";
	}

	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method

}
