package no.hvl.dat110.rpc;

import java.util.Arrays;

public class RPCUtils {

	public static byte[] marshallString(byte rpcid, String str) {

		byte[] encoded;

		// TODO: marshall RPC identifier and string into byte array

		byte[] bytes = str.getBytes();
		
		encoded = new byte[str.length()+1];
		
		encoded[0] = rpcid;
		
		for(int i = 1; i<=bytes.length;i++) {
			encoded[i] = bytes[i-1];
		}

		return encoded;
	}

	public static String unmarshallString(byte[] data) {

		String decoded;
		byte[] dekodet = new byte[data.length-1];
	
		for(int i = 0; i<dekodet.length;i++) {
		dekodet[i]=data[i+1];	
		}
		decoded = new String(dekodet);
		
		return decoded;
	}

	public static byte[] marshallVoid(byte rpcid) {

		byte[] encoded=new byte[1];

		// TODO: marshall RPC identifier in case of void type

		encoded[0]=rpcid;

		return encoded;

	}

	public static void unmarshallVoid(byte[] data) {

		// TODO: unmarshall void type
		return;
	}

	public static byte[] marshallBoolean(byte rpcid, boolean b) {

		byte[] encoded = new byte[2];

		encoded[0] = rpcid;

		if (b) {
			encoded[1] = 1;
		} else {
			encoded[1] = 0;
		}

		return encoded;
	}

	public static boolean unmarshallBoolean(byte[] data) {

		return (data[1] > 0);

	}

	public static byte[] marshallInteger(byte rpcid, int x) {

		byte[] encoded=new byte[5];
		
		String s = String.valueOf(x);
		// TODO: marshall RPC identifier and string into byte array
	
		encoded = marshallString(rpcid, s);

		return encoded;
	}

	public static int unmarshallInteger(byte[] data) {

		int decoded;
		// TODO: unmarshall integer contained in data
		String s = unmarshallString(data);
		
		decoded = Integer.parseInt(s);
		
		return decoded;

	}
}
