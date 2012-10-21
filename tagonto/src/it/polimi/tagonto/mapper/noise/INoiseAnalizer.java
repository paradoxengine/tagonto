package it.polimi.tagonto.mapper.noise;

import it.polimi.tagonto.TagontoException;

/**
 * A noise analyzer calculates noisyness of a string and tries to get a corrected version
 * of the string.  
 * @author Mauro Luigi Drago
 *
 */
public interface INoiseAnalizer 
{
	public void setString(String str);
	
	public void analize() throws TagontoException;
	
	public String correctString() throws TagontoException;
	
	public float getNoisyness() throws TagontoException;
}
