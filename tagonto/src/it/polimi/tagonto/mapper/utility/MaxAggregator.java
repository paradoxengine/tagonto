package it.polimi.tagonto.mapper.utility;

import it.polimi.tagonto.mapper.Mapping;

import java.util.Collection;
import java.util.Vector;

/**
 * The max mapping aggregator aggregates two mapping updating weights choosing the maximum weight.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class MaxAggregator implements IMappingAggregator 
{

	public Collection<Mapping> aggregate(Collection<Mapping> firstMappings,
			Collection<Mapping> secondMappings) 
	{
		Collection<Mapping> aggregatedMappings = new Vector<Mapping>();
		
		// iterate through first mappings
		for(Mapping firstMapping : firstMappings){
			boolean found = false;
			for(Mapping secondMapping: secondMappings){
				if (firstMapping.getConcept().equals(secondMapping.getConcept())){
					float newSignificance = 0;
					if (firstMapping.getSignificance() >= secondMapping.getSignificance()) newSignificance = firstMapping.getSignificance();
					else newSignificance = secondMapping.getSignificance();
					
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
						firstMapping.getSignificance());
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
						secondMapping.getSignificance());
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
