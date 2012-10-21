package it.polimi.tagonto.mapper;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.TagontoException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ontology pool keep a pool of loaded onthologies to increase performances.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class OntologyPool 
{//TODO add pool cleaning
	
	private static OntologyPool instance = new OntologyPool();
	
	private int poolSize = Configuration.getInstance().getONTOLOGY_POOL_SIZE();
	private Map<String, PoolEntry> pool = new ConcurrentHashMap<String, PoolEntry>();
	private Map<Integer, PoolEntry> poolCopy = new ConcurrentHashMap<Integer, PoolEntry>();
	
	private static class PoolEntry
	{
		private Ontology ont = null;
		private Calendar lastUse = new GregorianCalendar();
		
		private PoolEntry(Ontology ont)
		{
			this.ont = ont;
		}
		
		private Ontology getontology()
		{
			this.lastUse = new GregorianCalendar();
			return this.ont; 
		}
		
		private Calendar getLastUse()
		{
			return this.lastUse;
		}
	};
	
	public static OntologyPool getPool()
	{
		return OntologyPool.instance;
	}

	/**
	 * Gets the ontology identifier by the specified id.
	 *  
	 * @param uri the uri of the ontology.
	 * 
	 * @return the requested ontology. 
	 */
	public Ontology getOntology(String uri) throws TagontoException
	{
		if (this.pool.containsKey(uri)) return this.pool.get(uri).getontology();
		else{
			Ontology ont = new Ontology(uri);
			PoolEntry entry = new PoolEntry(ont);
			this.pool.put(uri, entry);
			this.poolCopy.put(entry.getontology().getId(), entry);
			return ont;
		}
	}
	
	public Ontology getOntology(int id)
	{
		if (this.poolCopy.containsKey(id)) return this.poolCopy.get(id).getontology();
		else return null;
	}
}
