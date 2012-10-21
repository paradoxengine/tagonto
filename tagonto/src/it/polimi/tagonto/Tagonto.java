package it.polimi.tagonto;

import it.polimi.tagonto.port.restlet.RestletPort;

public class Tagonto {

	public static void main(String[] args) 
	{
		String configFile = null;
		if (args.length == 1) configFile = args[0];
		
		try {
			Configuration.createConfiguration(configFile);
		} catch (TagontoException e){ 
			e.printStackTrace();
			return;
		}
		
		RestletPort port = RestletPort.getInstance();
		try {
			port.init();
		} catch (TagontoException e) {
			e.printStackTrace();
		}
	}
}
