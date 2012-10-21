package it.polimi.tagonto.mapper.plugins;

import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;

import java.util.Collection;

/**
 * A MatchPlugin is a plugin to calculate the similarity between a tag and
 * an ontology concept.
 * 
 * @author Mauro Luigi Drago
 *
 */
public interface IMatchPlugin 
{
	/**
	 * Inits the plugin.
	 * 
	 * @throws PluginException if the initing failed.
	 */
	public void init() throws PluginException;
	
	/**
	 * Sets the ontology against which the plugin will match the tag.
	 * 
	 * @param ontology the ontology.
	 */
	public void setOntology(Ontology ontology);
	
	/**
	 * Gets the ontology against which the plugin will match the tag.
	 * 
	 * @return the ontology.
	 */
	public Ontology getOntology();
	
	/**
	 * Sets the tag to be matched against the ontology.
	 * 
	 * @param tag the tag to be matched.
	 */
	public void setTag(Tag tag);
	
	/**
	 * Gets the tag to be matched against the ontology.
	 * 
	 * @return the tag.
	 */
	public Tag getTag();
	
	/**
	 * Runs the match.
	 * 
	 * @return a collection of possible mappings.
	 * @throws PluginException if the matching failed.
	 */
	public Collection<Mapping> run() throws PluginException;
}
