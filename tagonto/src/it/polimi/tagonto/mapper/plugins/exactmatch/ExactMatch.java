package it.polimi.tagonto.mapper.plugins.exactmatch;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;

import java.util.Collection;
import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;

public class ExactMatch implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(ExactMatch.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(ExactMatch.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		
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

	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(ExactMatch.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		Collection<OntClass> concepts = this.ont.getNamedConcepts(this.tag.getTag());
		Collection<Mapping> mappings = new Vector<Mapping>();
		
		// just do a simple match
		for(OntClass concept : concepts){
			if (concept.getLocalName().compareToIgnoreCase(this.tag.getTag()) == 0)
				mappings.add(new Mapping(this.tag, concept, this.ont, 1));
		}
		
		return mappings;
	}
}
