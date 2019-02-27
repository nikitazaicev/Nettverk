package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;

public class TemperatureDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		TemperatureSensor sn = new TemperatureSensor();

		// TODO - start

		Client client = new Client("TemperatureDevice", Common.BROKERHOST, Common.BROKERPORT);
		client.connect();

		// sensor device connects to a broker

		for (int i = 0; i < COUNT; i++) {
			
			client.publish(Common.TEMPTOPIC, String.valueOf(sn.read()));
		}

		client.disconnect();
		// TODO - end

		System.out.println("Temperature device stopping ... ");

	}
}
