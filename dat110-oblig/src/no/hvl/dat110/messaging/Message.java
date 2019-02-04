package no.hvl.dat110.messaging;

import java.util.Arrays;

public class Message {

	private byte[] payload;

	public Message(byte[] payload) {
		
		this.payload = payload; 
		
		// TODO: check for length within boundary
	}

	public Message() {
		super();
	}

	public byte[] getData() {
		return this.payload; 
	}

	public byte[] encapsulate() {
		
		byte[] encoded;
		
		// TODO
		// encapulate/encode the payload of the message
		encoded = new byte[128];
		encoded[0] = (byte) payload.length;
		for(int i = 1; i<=payload.length;i++) {
			encoded[i]=payload[i-1];
		}
		return encoded;
		
	}

	public void decapsulate(byte[] received) {

		// TODO
		// decapsulate data in received and put in payload
	   int size = received[0];
	   byte[] decoded = new byte[size];
	   for(int i = 0;i<size;i++) {
		   decoded[i]=received[i+1];
	   }
		payload = decoded;
	}
}
