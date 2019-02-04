package no.hvl.dat110.system.controller;

import no.hvl.dat110.rpc.RPCClient;
import no.hvl.dat110.rpc.RPCCommon;
import no.hvl.dat110.rpc.RPCServerStopStub;
import no.hvl.dat110.rpc.RPCUtils;

public class Controller  {
	
	private static int N = 5;
	
	public static void main (String[] args) {
		
		Display display;
		Sensor sensor;
		
		RPCClient displayclient,sensorclient;
		
		System.out.println("Controller starting ...");
				
		RPCServerStopStub stopdisplay = new RPCServerStopStub();
		RPCServerStopStub stopsensor = new RPCServerStopStub();
		
		displayclient = new RPCClient(Common.DISPLAYHOST,Common.DISPLAYPORT);
		sensorclient = new RPCClient(Common.SENSORHOST,Common.SENSORPORT);
		
		// TODO
		// create display and sensor object
		// create RPC clients for display device and sensor device
		// register RPC methods in the RPC layer
		display = new Display();
		sensor = new Sensor();
		
		
		displayclient.connect();
		sensorclient.connect();		
		
		displayclient.register(display);
		sensorclient.register(sensor);
		
		// register stop methods in the RPC middleware
		displayclient.register(stopdisplay);
		sensorclient.register(stopsensor);
		
		// TODO:
		// loop while reading from sensor and write to display via RPC
		
		
		for(int i = 0; i < N; i++) {
			int temp = sensor.read();
			display.write(temp+"");
			try {
				Thread.sleep(1234);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		stopdisplay.stop();
		stopsensor.stop();
	
		displayclient.disconnect();
		sensorclient.disconnect();
		
		System.out.println("Controller stopping ...");
		
	}
}
