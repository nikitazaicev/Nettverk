package no.hvl.dat110.system.controller;

import no.hvl.dat110.rpc.*;
import no.hvl.dat110.system.display.DisplayImpl;

public class Display extends RPCStub {

	private byte RPCID = 1;

	public void write(String message) {

		// TODO
		// implement marshalling, call 
		// and unmarshalling for write RPC method
		
		byte[] reply = RPCUtils.marshallString(RPCID, message);
		
		RPCUtils.unmarshallVoid(rmiclient.call(reply));
		
	}
}
