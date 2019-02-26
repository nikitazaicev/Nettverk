package no.hvl.dat110.system.controller;

import no.hvl.dat110.rpc.*;
import no.hvl.dat110.system.sensor.SensorImpl;

public class Sensor extends RPCStub {

	private byte RPCID = 2;
	
	public int read() {
		
		int temp;
		
		// TODO
		// implement marshalling, call 
		// and unmarshalling for read RPC method
		
		byte[] reply = RPCUtils.marshallVoid(RPCID);
		
		byte[] received = rmiclient.call(reply);
		
		temp = RPCUtils.unmarshallInteger(received);
		
		return temp;
	}
	
}
