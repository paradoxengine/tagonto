package it.polimi.tagonto.mapper.caching;

import java.util.Collection;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;

public class CompositeMappingCacher implements IMappingCacher
{
	private static CompositeMappingCacher instance = new CompositeMappingCacher();
	
	private DbMappingCacher dbCacher = DbMappingCacher.getInstance();
	private RdfMappingCacher rdfCacher = RdfMappingCacher.getInstance();
	
	private CompositeMappingCacher()
	{
	}
	
	public static CompositeMappingCacher getInstance()
	{
		return CompositeMappingCacher.instance;
	}
	
	public DbMappingCacher getDbCacher()
	{
		return this.dbCacher;
	}
	
	public RdfMappingCacher rdfCacher()
	{
		return this.rdfCacher;
	}
	
	public void cacheMappings(Collection<Mapping> mappings) 
		throws TagontoException
	{
		TagontoException e = null;
		
		try{
			this.dbCacher.cacheMappings(mappings);
		}catch(TagontoException e1){
			e = e1;
		}
		
		try{
			this.rdfCacher.cacheMappings(mappings);
		}catch(TagontoException e1){
			e = e1;
		}
		
		if (e != null) throw e;
	}
	
	public void cacheMapping(Mapping mapping) throws TagontoException
	{
		TagontoException e = null;
		
		try{
			this.dbCacher.cacheMapping(mapping);
		}catch(TagontoException e1){
			e = e1;
		}
		
		try{
			this.rdfCacher.cacheMapping(mapping);
		}catch(TagontoException e1){
			e = e1;
		}
		
		if (e != null) throw e;
	}
	
	public Collection<Mapping> getCachedMappings(Tag tag, Ontology ontology, User user) 
		throws TagontoException
	{
		// just ask to the db, it's faster than rdf
		return this.dbCacher.getCachedMappings(tag, ontology, user);
	}

	public Collection<Mapping> getAggregatedCachedMappings(Tag tag, Ontology ontology) 
		throws TagontoException 
	{
		// just ask to the db, it's faster than rdf
		return this.dbCacher.getAggregatedCachedMappings(tag, ontology);
	}
}
