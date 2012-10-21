package it.polimi.tagonto.mapper.plugins.linkchooser;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.Ontology.PropertyStat;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This plugin modifies the significance of canditate mappings analyzing the connection
 * of a concept with other specified concepts.
 * @author goldivan
 *
 */
public class LinkChooser implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;
	private Collection<Mapping> neighbourMappings = null;
	private float weight = -1f;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(LinkChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(LinkChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(LinkChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAPPINGS_NOT_SET));
		if (this.neighbourMappings == null) throw new PluginException(LinkChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAPPINGS_NOT_SET));
		if (this.weight < 0f || this.weight > 1f) throw new PluginException(LinkChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_WEIGHT_NOT_SET));
		
		this.isInited = true;
	}
	
	public void setOntology(Ontology ontology) 
	{
		this.ont = ontology;
	}

	public void setTag(Tag tag) 
	{
		this.tag = tag;
	}

	public Ontology getOntology() 
	{
		return this.ont;
	}

	public Tag getTag() 
	{
		return this.tag;
	}
	
	public float getWeight()
	{
		return this.weight;
	}
	
	public void setWeight(float weight)
	{
		this.weight = weight;
	}
	
	public void setMappings(Collection<Mapping> mappings)
	{
		this.mappings = mappings;
	}
	
	public void setNeighbourMappings(Collection<Mapping> neighbourMappings)
	{
		this.neighbourMappings = neighbourMappings;
	}

	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(LinkChooser.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
	    
		//if (this.neighbourMappings.size() == 0) return this.mappings;
		
	    Map<String,StatRecord> statRecords = new HashMap<String,StatRecord>();
	    
	    for(Mapping candidate : this.mappings){// iterate through candidate mappings
	    	if (!(statRecords.containsKey(candidate.getId()))) statRecords.put(candidate.getId(), new StatRecord());
	    	StatRecord candidateStat = statRecords.get(candidate.getId());
	    	
	    	for(Mapping neighbour : this.neighbourMappings){
	    		if (candidate.getConcept().equals(neighbour.getConcept())) candidateStat.incrementLinks(1);
	    		else{
	    			PropertyStat propStat = this.ont.getPropertyStat(candidate.getConcept(), neighbour.getConcept());
	    			if (propStat != null) candidateStat.incrementLinks(propStat.getCount());
	    		}	
	    	}
	    }
	    
		// now summarize statistics and modifiy significance of mappings
	    int maxLinks = 0;
	    float avgLinksNumber = 0;
	    
	    for(StatRecord record : statRecords.values()){
	    	if (record.getLinks() > maxLinks) maxLinks = record.getLinks();
	    	avgLinksNumber += record.getLinks();
	    }
	    avgLinksNumber = avgLinksNumber / statRecords.size();
	    
	    for(Mapping candidate : this.mappings){
	    	StatRecord stat = statRecords.get(candidate.getId());
	    	float increment = 0;
	    	
	    	if (avgLinksNumber == 0){ // no mapping is connected, probably useless mappings, decrease significativity
	    		increment = -0.2f * candidate.getSignificance();
	    		candidate.setSignificance(candidate.getSignificance() + increment);
	    		continue;
	    	}
	    	
	    	if (stat.getLinks() < avgLinksNumber){ // links < avg
	    		// decrement significance
	    		increment = stat.getLinks() / maxLinks;
	    		increment *= -1f;
	    	}else{ // links >= avg
	    		// increment significance
	    		increment = stat.getLinks() / maxLinks;
	    	}
	    	
	    	increment *= this.weight;
	    	candidate.setSignificance(candidate.getSignificance() + increment);
	    }
	    
	    return this.mappings;
	}
	
	private static class StatRecord
	{
		private int links = 0;
		
		private int getLinks()
		{
			return this.links;
		}
		
		private void incrementLinks(int increment)
		{
			this.links += increment;
		}
	}
}
