package it.polimi.tagonto.port.restlet;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.TagontoException;

import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * The restlet port provides a rest interface to the mapping library.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class RestletPort 
{
	private static RestletPort instance = new RestletPort();
	private Configuration config = Configuration.getInstance();
	
	private boolean isInited = false;
	private Component component = null;
	
	private RestletPort()
	{
	}
	
	public static RestletPort getInstance()
	{
		return RestletPort.instance;
	}
	
	public void init() throws TagontoException
	{
		if (this.isInited == true) throw new TagontoException("Restlet Port already inited");
		else{
			this.component = new Component();
			this.initServer();
		}
	}
	
	private void initServer() throws TagontoException
	{
		this.component.getServers().add(Protocol.HTTP, RestletPortConfig.LISTENING_PORT);
		
		TagontoApplication mainApp = new TagontoApplication();
		this.component.getDefaultHost().attach("", mainApp);
		try {
			this.component.start();
		} catch (Exception e) {
			throw new TagontoException(e);
		}
	}
}
