package it.polimi.tagonto.mapper.utility;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;

import java.util.Collection;

/**
 * A mapping aggregator aggregates different mappings updating their significance according
 * to a specific algorithm.
 * 
 * @author Mauro Luigi Drago
 *
 */
public interface IMappingAggregator 
{
	public Collection<Mapping> aggregate(Collection<Mapping> firstMappings, Collection<Mapping> secondMappings)
		throws TagontoException;
}
