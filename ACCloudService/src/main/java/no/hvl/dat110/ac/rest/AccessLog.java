package no.hvl.dat110.ac.rest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

public class AccessLog {
	
	private AtomicInteger cid;
	protected ConcurrentHashMap<Integer, AccessEntry> log;
	
	public AccessLog () {
		this.log = new ConcurrentHashMap<Integer,AccessEntry>();
		cid = new AtomicInteger(0);
	}

	// TODO: add an access entry for the message and return assigned id
	public int add(String message) {
		
		int id = cid.getAndIncrement();
		
		log.put(id, new AccessEntry(id,message));
		
		return id;
	}
		
	// TODO: retrieve a specific access entry 
	public AccessEntry get(int id) {
		return log.get(id);
	}
	
	// TODO: clear the access entry log
	public void clear() {
		log.clear();
	}
	
	// TODO: JSON representation of the access log
	public String toJson () {

	 	Gson gson = new Gson();
	 	
		String json = gson.toJson(log,AccessLog.class);
    	
    	return json;
    }
}
