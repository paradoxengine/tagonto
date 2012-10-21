package it.polimi.tagonto.mapper;

import it.polimi.tagonto.TagontoException;

import java.util.Collection;

/**
 * Mappers execute the matching of a tag against concepts in an ontology.<br>
 * Mappers use MatchPlugins to calculate the match and implement an algorithm specific for
 * the mapper to join the results of the plugins.
 * 
 * @author Mauro Luigi Drago.
 *
 */
public interface IMapper 
{
	/**
	 * Sets the ontology against which the tag should be matched.
	 * 
	 * @param ontology the ontology.
	 */
	public void setontology(Ontology ontology);
	
	/**
	 * Sets the tag which should be matched against the ontology.
	 * @param tag the tag.
	 */
	public void setTag(Tag tag);
	
	/**
	 * Sets the user who requested the match.
	 * @param userId the id of the user
	 */
	public void setUser(User user);
	
	/**
	 * Inits the mapper.
	 * 
	 * @throws TagontoException if the initing failed.
	 */
	public void init() throws MapperException;
	
	/**
	 * Runs the mapper.
	 * 
	 * @return the list of mappings identified.
	 * 
	 * @throws MapperException if the mapping failed.
	 */
	public Collection<Mapping> run() throws MapperException;
}
