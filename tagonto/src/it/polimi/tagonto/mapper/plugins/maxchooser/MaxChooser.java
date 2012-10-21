package it.polimi.tagonto.mapper.plugins.maxchooser;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class MaxChooser implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;
	private int maxMappings = 1;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(MaxChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(MaxChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(MaxChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAPPINGS_NOT_SET));
		if (this.maxMappings < 0)  throw new PluginException(MaxChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_SIMPLE_CHOOSER_MAX_MAPPINGS_NOT_SET));
		
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
	
	public int getMaxMappings()
	{
		return this.maxMappings;
	}
	
	public void setMaxMappings(int maxMappings)
	{
		this.maxMappings = maxMappings;
	}
	
	public void setMappings(Collection<Mapping> mappings)
	{
		this.mappings = mappings;
	}

	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(MaxChooser.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		List<Mapping> results = new ArrayList<Mapping>(this.mappings);
		Collections.sort(results);
		
		List<Mapping> invertedResults = new ArrayList<Mapping>();
		for(Mapping mapping : results){
			invertedResults.add(0, mapping);
		}
		
		int counter = 0;
		float lastSignificance = 0;
		boolean flag = false;
		
		ListIterator<Mapping> it = invertedResults.listIterator();
		while(it.hasNext()){
			Mapping mapping = it.next();
			if (flag == false){
				flag = true;
				if (counter >= this.maxMappings){
					it.remove();
					continue;
				}
				counter = 1;
				lastSignificance = mapping.getSignificance();
				continue;
			}
			
			if (mapping.getSignificance() == lastSignificance) continue;
			else{
				if (counter >= this.maxMappings) it.remove();
				else counter++;
			}
		}
		
		/*
		if (this.mappings.isEmpty() == true) return result;
		else{
			Mapping choosen = this.mappings.iterator().next();
			for(Mapping mapping : this.mappings){
				if (mapping.getSignificance() > choosen.getSignificance()){
					choosen = mapping;
					result.clear();
					result.add(choosen);
				}else{
					if (mapping.getSignificance() == choosen.getSignificance()) result.add(mapping);
				}
			}
		}*/
		
		return invertedResults;
	}
}
