package no.hvl.dat110.system.sensor;

import no.hvl.dat110.rpc.RPCServer;
import no.hvl.dat110.system.controller.Common;

public class SensorDevice {

	public static void main(String[] args) {
		
		System.out.println("Sensor server starting ...");
		
		SensorImpl sensor = new SensorImpl();
		
		RPCServer sensorserver = new RPCServer(Common.SENSORPORT);
		
	    sensorserver.register(2,sensor);
		
		sensorserver.run();
		
		sensorserver.stop();
		
		System.out.println("Sensor server stopping ...");
		
	}
}
