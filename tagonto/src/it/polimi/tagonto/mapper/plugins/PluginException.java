package it.polimi.tagonto.mapper.plugins;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;

public class PluginException extends TagontoException 
{
	private static final long serialVersionUID = 3166793466929211521L;
	
	private String message = null;

	public PluginException(Class<? extends IMatchPlugin> plugin, String msg)
	{
		super();
		this.message = String.format(Resources.MSG_PLUGIN_EXCEPTION, plugin.getSimpleName()) + msg;
	}
	
	public PluginException(Class<? extends IMatchPlugin> plugin, String msg, Throwable e)
	{
		super(e);
		this.message = String.format(Resources.MSG_PLUGIN_EXCEPTION, plugin.getSimpleName()) + msg;
	}

	@Override
	public String getMessage() 
	{
		return this.message;
	}
}
