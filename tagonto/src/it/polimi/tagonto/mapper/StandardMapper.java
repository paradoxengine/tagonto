package it.polimi.tagonto.mapper;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.caching.CompositeMappingCacher;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.plugins.friendschooser.FriendsChooser;
import it.polimi.tagonto.mapper.plugins.googlechooser.GoogleChooser;
import it.polimi.tagonto.mapper.plugins.maxchooser.MaxChooser;
import it.polimi.tagonto.mapper.plugins.thresholdchooser.ThresholdChooser;
import it.polimi.tagonto.mapper.utility.MaxAggregator;

import java.util.Collection;
import java.util.Vector;

/**
 * The standard mapper uses the plugin in sequence to determine the mapping of a tag on a
 * specified onthology.<br>
 * The process consists the following phases:
 * <ul>
 * <li> Exact Match Phase: the mapper uses syntactic and semantic plugins to determine possible mappings of the tag.
 * <li> Derived Match Phase: this is a compound phase
 * 		<ul>
 * 			<li> Noise Analysis Phase: the mapper analyzes the tag to reduce possible noise (misspellings, compound words...)
 * 			<li> Match Phase : results of the preceding phase are used to generate new possible mappings of to adjust significativity. 
 * 		</ul>    
 * 
 * <li> Choosing Phase : the mapper uses structural and heuristic plugins to purge the list of candidate mappings and choose
 * 		the best ones.
 * </ul>
 * @author Mauro Luigi Drago
 *
 */
public class StandardMapper implements IMapper 
{
	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private boolean greedyMatch = true;
	private User user = null;
	private int maxResults = 3;
	
	public void init() throws MapperException 
	{
		if (this.ont == null) throw new MapperException(StandardMapper.class, 
				String.format(Resources.MSG_MAPPER_NOT_INITED, Resources.MSG_MAPPER_ONT_NOT_SET));
		if (this.tag == null) throw new MapperException(StandardMapper.class, 
				String.format(Resources.MSG_MAPPER_NOT_INITED, Resources.MSG_MAPPER_TAG_NOT_SET));
		if (this.user == null) throw new MapperException(StandardMapper.class, 
				String.format(Resources.MSG_MAPPER_NOT_INITED, Resources.MSG_MAPPER_USER_NOT_SET));
		
		this.isInited = true;
	}
	
	public void setontology(Ontology ontology) 
	{
		this.ont = ontology;
	}

	public void setTag(Tag tag) 
	{
		this.tag = tag;
	}
	
	public void setGreedyMatch(boolean value)
	{
		this.greedyMatch = value;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}

	public Collection<Mapping> run() throws MapperException 
	{
		if (this.isInited == false) throw new MapperException(StandardMapper.class,
				String.format(Resources.MSG_MAPPER_NOT_INITED, ""));
		
		Collection<Mapping> aggregatedMappings = new Vector<Mapping>();
		
		// first do a greedy match
		try{	
			GreedyMapper mapper = new GreedyMapper();
			mapper.setTag(this.tag);
			mapper.setontology(this.ont);
			mapper.setUser(this.user);
			mapper.init();
			aggregatedMappings = mapper.run();
		} catch (Exception e) {
			throw new MapperException(StandardMapper.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
		
		Collection<Mapping> alreadyAnalyzedMappings = new Vector<Mapping>();
		Collection<Mapping> toAnalyzeMappings = new Vector<Mapping>();
		for(Mapping mapping : aggregatedMappings){
			if (mapping.getMappers().contains(this.getClass())) alreadyAnalyzedMappings.add(mapping);
			else toAnalyzeMappings.add(mapping);
		}
		
		toAnalyzeMappings = this.chooseResult(toAnalyzeMappings);
		
		MaxAggregator aggregator = new MaxAggregator();
		aggregatedMappings = aggregator.aggregate(alreadyAnalyzedMappings, toAnalyzeMappings);
		
		try{
			ThresholdChooser chooser = new ThresholdChooser();
			chooser.setTag(this.tag);
			chooser.setOntology(this.ont);
			chooser.setMappings(aggregatedMappings);
			chooser.setThreshold(0.2f);
			chooser.init();
			aggregatedMappings = chooser.run();
		} catch (Exception e) {
			throw new MapperException(StandardMapper.class, String.format(Resources.MSG_PLUGIN_EXCEPTION, e.getMessage()), e);
		}
		
		try {
			Mapping.addMapper(aggregatedMappings, this);
			CompositeMappingCacher.getInstance().cacheMappings(aggregatedMappings);
			return aggregatedMappings;
		} catch (TagontoException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
	}
	
	
	private Collection<Mapping> chooseResult(Collection<Mapping> mappings) throws MapperException
	{
		if (this.greedyMatch == true){ // choose the one with the highest significance
			try {
				MaxChooser chooser = new MaxChooser();
				chooser.setTag(this.tag);
				chooser.setOntology(this.ont);
				chooser.setMappings(mappings);
				chooser.setMaxMappings(this.maxResults);
				chooser.init();
				return chooser.run();
			} catch (PluginException e) {
				throw new MapperException(StandardMapper.class,
						String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
			}
		}else{
			Collection<Mapping> result = null;
			try{
				FriendsChooser chooser = new FriendsChooser();
				chooser.setTag(this.tag);
				chooser.setOntology(this.ont);
				chooser.setMappings(mappings);
				chooser.init();
				result = chooser.run();
			}catch(PluginException e){
				throw new MapperException(StandardMapper.class,
						String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
			}
			
			try{
				GoogleChooser chooser = new GoogleChooser();
				chooser.setTag(this.tag);
				chooser.setOntology(this.ont);
				chooser.setMappings(mappings);
				chooser.init();
				result = chooser.run();
			}catch(PluginException e){
				throw new MapperException(StandardMapper.class,
						String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
			}
			
			return result;
		}
	}
}
