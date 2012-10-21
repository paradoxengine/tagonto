package it.polimi.tagonto.mapper.plugins.sillymappingsfilter;

import java.util.Collection;
import java.util.Vector;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;

public class SillyMappingsFilter implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;
	
	private static final String OWL_THING_URI = "http://www.w3.org/2002/07/owl#Thing";
	private static final String OWL_NOTHING_URI = "http://www.w3.org/2002/07/owl#Nothing";
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(SillyMappingsFilter.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(SillyMappingsFilter.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(SillyMappingsFilter.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAPPINGS_NOT_SET));
		
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
	
	public void setMappings(Collection<Mapping> mappings)
	{
		this.mappings = mappings;
	}
	
	public Collection<Mapping> getMappings()
	{
		return this.mappings;
	}

	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(SillyMappingsFilter.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		Collection<Mapping> results = new Vector<Mapping>();
		for(Mapping mapping : this.mappings){
			if (mapping.getConcept().getURI().equalsIgnoreCase(SillyMappingsFilter.OWL_THING_URI)) continue;
			if (mapping.getConcept().getURI().equalsIgnoreCase(SillyMappingsFilter.OWL_NOTHING_URI)) continue;
			results.add(mapping);
		}
		
		return results;
	}
	
	
}
