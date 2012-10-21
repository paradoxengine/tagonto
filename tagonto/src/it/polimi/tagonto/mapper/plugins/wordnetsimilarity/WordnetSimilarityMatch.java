package it.polimi.tagonto.mapper.plugins.wordnetsimilarity;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.utility.WordnetResource;

import java.util.Collection;
import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;

public class WordnetSimilarityMatch implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(WordnetSimilarityMatch.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(WordnetSimilarityMatch.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		
		try {
			WordnetResource.initWordnet();
		} catch (TagontoException e) {
			throw new PluginException(WordnetSimilarityMatch.class, 
					String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_WORDNET_NOT_INITED));
		}
		
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
		if (this.isInited == false) throw new PluginException(WordnetSimilarityMatch.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		Collection<Mapping> results = new Vector<Mapping>();
		
		for(OntClass concept : this.ont.getNamedConcepts()){
			// array objects :
			// 1 - equivalent
			// 2 - hyponim
			// 3 - hyperonim
			float[] similarities = WordNetSimilarityCore.computeSimilarity(concept.getLocalName(), this.tag.getTag());
			
			float similarity = 0;
			
			//average
			for(int i = 0; i < 3; i++){
				similarity += similarities[i];
			}
			similarity = similarity / 3f;
			
			results.add(new Mapping(this.tag, concept, this.ont, similarity));
		}
		
		return results;
	}
}
