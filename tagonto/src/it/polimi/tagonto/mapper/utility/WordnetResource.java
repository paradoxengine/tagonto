package it.polimi.tagonto.mapper.utility;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;

import java.io.FileInputStream;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.dictionary.Dictionary;

public class WordnetResource 
{
	private static WordnetResource instance = new WordnetResource();
	private static boolean JWNL_INITIALIZED = false;
	
	public static void initWordnet() throws TagontoException
	{
		if (WordnetResource.JWNL_INITIALIZED == false){
			try {
				JWNL.initialize(new FileInputStream(Configuration.getInstance().getWORDNET_JWNL_CONFIG_FILE()));
				WordnetResource.JWNL_INITIALIZED = true;
			} catch (Exception e) {
				throw new TagontoException(Resources.MSG_PLUGIN_WORDNET_NOT_INITED);
			}
		}
	}
	
	public Dictionary getDictionary() throws TagontoException
	{
		WordnetResource.initWordnet();
		return Dictionary.getInstance(); 
	}
	
	public static WordnetResource getInstance()
	{
		return WordnetResource.instance;
	}
}
