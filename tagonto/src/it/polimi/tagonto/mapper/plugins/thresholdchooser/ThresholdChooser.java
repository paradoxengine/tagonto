package it.polimi.tagonto.mapper.plugins.thresholdchooser;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;

import java.util.Collection;
import java.util.Vector;

public class ThresholdChooser implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;
	private float threshold = -1f;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(ThresholdChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(ThresholdChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(ThresholdChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAPPINGS_NOT_SET));
		if (this.threshold < 0f || this.threshold > 1f) throw new PluginException(ThresholdChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_THRESHOLD_NOT_SET));
		
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
	
	public float getThreshold()
	{
		return this.threshold;
	}
	
	public void setThreshold(float threshold)
	{
		this.threshold = threshold;
	}
	
	public void setMappings(Collection<Mapping> mappings)
	{
		this.mappings = mappings;
	}

	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(ThresholdChooser.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		Collection<Mapping> result = new Vector<Mapping>();
		
		for(Mapping mapping : this.mappings){
			if (mapping.getSignificance() >= this.threshold) result.add(mapping);
		}
		
		return result;
	}
}
