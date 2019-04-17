package no.hvl.dat110.broker;

import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.MessageUtils;
import no.hvl.dat110.messagetransport.Connection;

public class ClientSession extends Stopable{

	private String user;
	private Connection connection;
	private Dispatcher dispatcher;


	public ClientSession(String user, Connection connection) {
		super("ClientSession");
		this.user = user;
		this.connection = connection;
		this.dispatcher = null;
	}
	
	@Override
	public void doProcess() {
		Message msg = null;

		if (hasData()) {
			msg = receive();
		}
	
		if (msg != null) {
			dispatch(this, msg);
		}
			
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	}
	

	private void dispatch(ClientSession clientSession, Message msg) {
		dispatcher.dispatch(this, msg);
		
	}

	public void disconnect() {

		if (connection != null) {
			connection.close();
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public void send(Message message) {

		MessageUtils.send(connection, message);
	}

	public boolean hasData() {

		return connection.hasData();
	}

	public Message receive() {

		Message msg = MessageUtils.receive(connection);

		return msg;
	}

	
	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}



}
