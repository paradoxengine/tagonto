package it.polimi.tagonto.mapper.plugins.cachedmatch;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;
import it.polimi.tagonto.mapper.caching.CompositeMappingCacher;
import it.polimi.tagonto.mapper.caching.IMappingCacher;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.plugins.exactmatch.ExactMatch;

import java.util.Collection;

public class CachedMatch implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private User user = null;
	
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(CachedMatch.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(CachedMatch.class, 
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
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public User getUser()
	{
		return this.user;
	}

	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(ExactMatch.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		IMappingCacher cacher = CompositeMappingCacher.getInstance();
		
		try {
			if (this.user != null){ // check if there exist some cached mappings for this specific user
				Collection<Mapping> results = cacher.getCachedMappings(this.tag, this.ont, this.user);
				if (results.size() != 0) return results;
			}
			
			// otherwise search for global mappings
			return cacher.getAggregatedCachedMappings(this.tag, this.ont);
		} catch (TagontoException e) {
			throw new PluginException(CachedMatch.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
	}
}
