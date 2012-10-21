package it.polimi.tagonto.mapper.plugins.friendschooser;

import java.util.Collection;
import java.util.Vector;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.GreedyMapper;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.plugins.linkchooser.LinkChooser;
import it.polimi.tagonto.mapper.plugins.maxchooser.MaxChooser;
import it.polimi.tagonto.mapper.plugins.thresholdchooser.ThresholdChooser;

/**
 * This plugin modifies the significance of the mappings for a tag analyzing its neighbour tags.
 *  
 * @author Mauro Luigi Drago
 *
 */
public class FriendsChooser implements IMatchPlugin 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;

	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(FriendsChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(FriendsChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(FriendsChooser.class, 
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
	
	public Collection<Mapping> run() throws PluginException 
	{
		if (this.isInited == false) throw new PluginException(FriendsChooser.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		Collection<Tag> friendTags = this.tag.getFriends();
		
		// now do a greedy match for friend tags
		Collection<Mapping> friendMappings = new Vector<Mapping>();
		for(Tag friendTag : friendTags){
			try{	
				Collection<Mapping> tempMappings = null;
				GreedyMapper mapper = new GreedyMapper();
				mapper.setTag(friendTag);
				mapper.setontology(this.ont);
				mapper.setUser(Configuration.getInstance().getDATABASE_TAGONTO_USER());
				mapper.init();
				tempMappings = mapper.run();
				
				MaxChooser chooser = new MaxChooser();
				chooser.setMappings(tempMappings);
				chooser.setMaxMappings(1);
				chooser.setOntology(this.ont);
				chooser.setTag(this.tag);
				chooser.init();
				tempMappings = chooser.run();
				
				friendMappings.addAll(tempMappings);
			} catch (Exception e) {
				throw new PluginException(FriendsChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
			}
		}
		
		if (friendTags.size() == 0) return this.mappings;
		
		// now purge results
		try{
			ThresholdChooser chooser = new ThresholdChooser();
			chooser.setMappings(friendMappings);
			chooser.setOntology(this.ont);
			chooser.setTag(this.tag);
			chooser.setThreshold(0.3f);
			chooser.init();
			friendMappings = chooser.run();
		}catch(PluginException e){
			throw new PluginException(FriendsChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
		
		// do a simple choose
		try{
			LinkChooser chooser = new LinkChooser();
			chooser.setTag(this.tag);
			chooser.setOntology(this.ont);
			chooser.setMappings(this.mappings);
			chooser.setNeighbourMappings(friendMappings);
			chooser.setWeight(0.2f);
			chooser.init();
			return chooser.run();
		}catch(PluginException e){
			throw new PluginException(FriendsChooser.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
	}

}
