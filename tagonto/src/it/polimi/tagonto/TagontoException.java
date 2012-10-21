package it.polimi.tagonto;

import org.apache.commons.lang.exception.NestableException;

public class TagontoException extends NestableException
{
	private static final long serialVersionUID = 25243985327995672L;

	public TagontoException() 
	{
		super();
	}

	public TagontoException(String arg0, Throwable arg1) 
	{
		super(arg0, arg1);
	}

	public TagontoException(String arg0) 
	{
		super(arg0);
	}

	public TagontoException(Throwable arg0) 
	{
		super(arg0);
	}
}
