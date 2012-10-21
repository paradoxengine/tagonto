package it.polimi.tagonto.mapper.caching;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;

import java.util.Collection;

public interface IMappingCacher extends IResourceCacher 
{
	/**
	 * Caches the specified mappings on the cacher media.
	 * @param mappings the mappings to cache
	 * @param userId the id of the user which requested the caching.
	 * @param ontology the ontology to which mappings refer.
	 * 
	 * @throws TagontoException if the operation failed.
	 */
	public void cacheMappings(Collection<Mapping> mappings) 
		throws TagontoException;
	
	/**
	 * Caches the specified mapping on the cacher media.
	 * @param mapping the mapping to cache
	 * 
	 * @throws TagontoException if the operation failed.
	 */
	public void cacheMapping(Mapping mapping) throws TagontoException;
	
	/**
	 * Gets all the cached mappings for the specified tag, ontology and user.
	 * 
	 * @param tag the tag to be matched.
	 * @param ontology the ontology against which the tag should be matched.
	 * @param user the user who inserted this mapping.
	 * 
	 * @return the cached mappings.
	 * @throws TagontoException if the operation failed.
	 */
	public Collection<Mapping> getCachedMappings(Tag tag, Ontology ontology, User user) 
		throws TagontoException;
	
	/**
	 * Gets all the cached mappings for the specified tag, ontology and user.
	 * 
	 * @param tag the tag to be matched.
	 * @param ontology the ontology against which the tag should be matched.
	 * @param user the user who inserted this mapping.
	 * 
	 * @return the cached mappings.
	 * @throws TagontoException if the operation failed.
	 */
	public Collection<Mapping> getAggregatedCachedMappings(Tag tag, Ontology ontology) 
		throws TagontoException;
}
