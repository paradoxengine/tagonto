package it.polimi.tagonto.mapper.utility;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;

import java.util.Collection;
import java.util.Vector;

/**
 * The weight mapping aggregator aggregates two different mappings using weights.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class WeightAggregator implements IMappingAggregator 
{
	private float secondMappingsWeight = -1f;
	
	public void setSecondMappingsWeight(float weight)
	{
		this.secondMappingsWeight = weight;
	}
	
	public Collection<Mapping> aggregate(Collection<Mapping> firstMappings,
			Collection<Mapping> secondMappings) throws TagontoException 
	{
		if (this.secondMappingsWeight < 0 || this.secondMappingsWeight > 1)
			throw new TagontoException(Resources.MSG_PLUGIN_WEIGHT_NOT_SET);
		
		float firstMappingsWeight = 1f - this.secondMappingsWeight;
		Collection<Mapping> aggregatedMappings = new Vector<Mapping>();
		
		// iterate through first mappings
		for(Mapping firstMapping : firstMappings){
			boolean found = false;
			for(Mapping secondMapping: secondMappings){
				if (firstMapping.getConcept().equals(secondMapping.getConcept())){
					float newSignificance = firstMapping.getSignificance() * firstMappingsWeight + 
											secondMapping.getSignificance() * this.secondMappingsWeight;
					
					Mapping newMapping = new Mapping(
							firstMapping.getTag(), 
							firstMapping.getConcept(),
							firstMapping.getOntology(),
							firstMapping.getUser(),
							newSignificance);
					aggregatedMappings.add(newMapping);
					found = true;
					break;
				}
			}
			if (found == false){
				Mapping newMapping = new Mapping(
						firstMapping.getTag(), 
						firstMapping.getConcept(),
						firstMapping.getOntology(),
						firstMapping.getUser(),
						firstMapping.getSignificance() * firstMappingsWeight);
				aggregatedMappings.add(newMapping);
			}
		}
		
		// iterate through second mappings
		for(Mapping secondMapping : secondMappings){
			if (this.findSimilarMapping(secondMapping, aggregatedMappings) == null){
				Mapping newMapping = new Mapping(
						secondMapping.getTag(), 
						secondMapping.getConcept(),
						secondMapping.getOntology(),
						secondMapping.getUser(),
						secondMapping.getSignificance() * this.secondMappingsWeight);
				aggregatedMappings.add(newMapping);
			}
		}
		
		return aggregatedMappings;
	}
	
	private Mapping findSimilarMapping(Mapping mappingToSearch, Collection<Mapping> mappings)
	{
		for(Mapping mapping : mappings){
			if (mapping.getTag().getTag().equals(mappingToSearch.getTag().getTag())){
				if (mapping.getConcept().getURI().equals(mappingToSearch.getConcept().getURI()))
					return mapping;
			}
		}
		
		return null;
	}

}
