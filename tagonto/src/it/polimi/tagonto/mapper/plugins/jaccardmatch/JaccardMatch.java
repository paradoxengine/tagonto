package it.polimi.tagonto.mapper.plugins.jaccardmatch;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.plugins.exactmatch.ExactMatch;

import java.util.Collection;
import java.util.Vector;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

import com.hp.hpl.jena.ontology.OntClass;

public class JaccardMatch implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(JaccardMatch.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(JaccardMatch.class, 
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
		
		Collection<OntClass> concepts = this.ont.getNamedConcepts();
		Collection<Mapping> mappings = new Vector<Mapping>();
		
		// calculate the Levenshtein measure for every tag concept pair
		for(OntClass concept : concepts){
			float maxLen = this.tag.getTag().length();
			if (maxLen < concept.getLocalName().length()) {
				maxLen = concept.getLocalName().length();
			}
			
			JaccardSimilarity comparer = new JaccardSimilarity();
			float measure = comparer.getSimilarity(this.tag.getTag().toLowerCase(), concept.getLocalName().toLowerCase());
			
			mappings.add(new Mapping(this.tag, concept, this.ont, measure));
		}
		
		return mappings;
	}
}
