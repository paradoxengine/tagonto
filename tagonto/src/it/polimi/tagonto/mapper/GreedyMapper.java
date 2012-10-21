package it.polimi.tagonto.mapper;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.caching.CompositeMappingCacher;
import it.polimi.tagonto.mapper.caching.IMappingCacher;
import it.polimi.tagonto.mapper.noise.GoogleNoiseAnalyzer;
import it.polimi.tagonto.mapper.plugins.PluginException;
import it.polimi.tagonto.mapper.plugins.cachedmatch.CachedMatch;
import it.polimi.tagonto.mapper.plugins.exactmatch.ExactMatch;
import it.polimi.tagonto.mapper.plugins.jaccardmatch.JaccardMatch;
import it.polimi.tagonto.mapper.plugins.levenshtein.LevenshteinMatch;
import it.polimi.tagonto.mapper.plugins.maxchooser.MaxChooser;
import it.polimi.tagonto.mapper.plugins.sillymappingsfilter.SillyMappingsFilter;
import it.polimi.tagonto.mapper.plugins.wordnetsimilarity.WordnetSimilarityMatch;
import it.polimi.tagonto.mapper.utility.MaxAggregator;
import it.polimi.tagonto.mapper.utility.WeightAggregator;

import java.util.Collection;
import java.util.Vector;

/**
 * The greedy mapper uses only syntactic plugins to match a tag on a concept of a specified onthology.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class GreedyMapper implements IMapper 
{
	private boolean isInited = false;
	private IMappingCacher mappingCacher = CompositeMappingCacher.getInstance();
	
	private Ontology ont = null;
	private Tag tag = null;
	private User user = null;
	private int maxResults = 3;
	
	public void init() throws MapperException 
	{
		if (this.ont == null) throw new MapperException(GreedyMapper.class, 
				String.format(Resources.MSG_MAPPER_NOT_INITED, Resources.MSG_MAPPER_ONT_NOT_SET));
		if (this.tag == null) throw new MapperException(GreedyMapper.class, 
				String.format(Resources.MSG_MAPPER_NOT_INITED, Resources.MSG_MAPPER_TAG_NOT_SET));
		if (this.user == null) throw new MapperException(GreedyMapper.class, 
				String.format(Resources.MSG_MAPPER_NOT_INITED, Resources.MSG_MAPPER_USER_NOT_SET));
		
		this.isInited = true;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public void setontology(Ontology ontology) 
	{
		this.ont = ontology;
	}

	public void setTag(Tag tag) 
	{
		this.tag = tag;
	}
	
	public Collection<Mapping> run() throws MapperException 
	{
		if (this.isInited == false) throw new MapperException(GreedyMapper.class,
				String.format(Resources.MSG_MAPPER_NOT_INITED, ""));
		
		Collection<Mapping> aggregatedMappings = new Vector<Mapping>();
		
		// cached match
		try {
			CachedMatch plugin = new CachedMatch();
			plugin.setOntology(this.ont);
			plugin.setTag(this.tag);
			plugin.setUser(this.user);
			plugin.init();
			Collection<Mapping> mappings = plugin.run();
			if (mappings.size() != 0){ // exact match found
				if (mappings.size() == 1) aggregatedMappings = mappings;
				else{ // choose the best
					MaxChooser chooser = new MaxChooser();
					chooser.setOntology(this.ont);
					chooser.setTag(this.tag);
					chooser.setMappings(mappings);
					chooser.init();
					aggregatedMappings = chooser.run();
				}
				return aggregatedMappings;
			}
		} catch (Exception e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		// exact match
		try {
			ExactMatch plugin = new ExactMatch();
			plugin.setOntology(this.ont);
			plugin.setTag(this.tag);
			plugin.init();
			Collection<Mapping> mappings = plugin.run();
			if (mappings.size() != 0){ // exact match found
				if (mappings.size() == 1) aggregatedMappings = mappings;
				else{ // choose the best
					MaxChooser chooser = new MaxChooser();
					chooser.setOntology(this.ont);
					chooser.setTag(this.tag);
					chooser.setMappings(mappings);
					chooser.init();
					aggregatedMappings = chooser.run();
				}
				
				Mapping.addMapper(aggregatedMappings, this);
				this.mappingCacher.cacheMappings(aggregatedMappings);
				return aggregatedMappings;
			}
		} catch (Exception e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		// Levenstein Match
		Collection<Mapping> levenshteinMappings = null;
		try{
			LevenshteinMatch plugin = new LevenshteinMatch();
			plugin.setOntology(this.ont);
			plugin.setTag(this.tag);
			plugin.init();
			levenshteinMappings = plugin.run();
		} catch (PluginException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		MaxAggregator maxAggregator = new MaxAggregator();
		aggregatedMappings = maxAggregator.aggregate(aggregatedMappings, levenshteinMappings);
		
		Collection<Mapping> jaccardMappings = null;
		try{
			JaccardMatch plugin = new JaccardMatch();
			plugin.setOntology(this.ont);
			plugin.setTag(this.tag);
			plugin.init();
			jaccardMappings = plugin.run();
		} catch (PluginException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		WeightAggregator weightAggregator = new WeightAggregator();
		weightAggregator.setSecondMappingsWeight(0.5f);
		try {
			aggregatedMappings = weightAggregator.aggregate(aggregatedMappings, jaccardMappings);
		} catch (TagontoException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		// Wordnet Match
		Collection<Mapping> wordnetMappings = null;
		try{
			WordnetSimilarityMatch plugin = new WordnetSimilarityMatch();
			plugin.setOntology(this.ont);
			plugin.setTag(this.tag);
			plugin.init();
			wordnetMappings = plugin.run();
		} catch (PluginException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		// aggregate results
		aggregatedMappings = maxAggregator.aggregate(aggregatedMappings, wordnetMappings);
		
		// check if the word was not mispelled
		GoogleNoiseAnalyzer bigGNoise = new GoogleNoiseAnalyzer();
		bigGNoise.setString(this.tag.getTag());
		float noise = 0;
		try {
			noise = bigGNoise.getNoisyness();
		} catch (TagontoException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		if (noise > Configuration.getInstance().getGREEDY_MAPPER_NOISE_THRESHOLD()){ // noise threshold overcome
			// repeat the greedy mapping with the corrected string
			Collection<Mapping> newMappings = null;
			try {
				GreedyMapper newMapper = new GreedyMapper();
				newMapper.setontology(this.ont);
				newMapper.setTag(new Tag(bigGNoise.correctString()));
				newMapper.setUser(Configuration.getInstance().getDATABASE_TAGONTO_USER());
				newMapper.init();
				newMappings = newMapper.run();
			} catch (TagontoException e) {
				throw new MapperException(GreedyMapper.class,
						String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
			}
			
			Collection<Mapping> newMappingsCorrected = new Vector<Mapping>();
			// now change the tag of the mappings
			for(Mapping mapping : newMappings){
				newMappingsCorrected.add(
						new Mapping(
								this.tag, mapping.getConcept(), 
								mapping.getOntology(), 
								mapping.getUser(),
								mapping.getSignificance()
								)
						);
			}
			
			// now merge results
			aggregatedMappings = maxAggregator.aggregate(aggregatedMappings, newMappingsCorrected);
		}
		
		// purge results
		try{
			SillyMappingsFilter sillyFilter = new SillyMappingsFilter();
			sillyFilter.setTag(this.tag);
			sillyFilter.setOntology(this.ont);
			sillyFilter.setMappings(aggregatedMappings);
			sillyFilter.init();
			aggregatedMappings = sillyFilter.run();
			
			MaxChooser mChooser = new MaxChooser();
			mChooser.setOntology(this.ont);
			mChooser.setTag(this.tag);
			mChooser.setMappings(aggregatedMappings);
			mChooser.setMaxMappings(this.maxResults);
			mChooser.init();
			aggregatedMappings = mChooser.run();
		} catch (PluginException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
		
		try {
			Mapping.addMapper(aggregatedMappings, this);
			this.mappingCacher.cacheMappings(aggregatedMappings);
			return aggregatedMappings;
		} catch (TagontoException e) {
			throw new MapperException(GreedyMapper.class,
					String.format(Resources.MSG_MAPPER_MAPPING_FAILED, e.getMessage(), e));
		}
	}
}
