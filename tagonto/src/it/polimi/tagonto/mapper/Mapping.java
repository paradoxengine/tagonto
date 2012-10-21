package it.polimi.tagonto.mapper;

import it.polimi.tagonto.Configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * A mapping is the result of a similarity match between a tag and a concept of
 * an ontology.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class Mapping implements Comparable
{
	private Tag tag = null;
	private Ontology ontology = null;
	private OntClass concept = null;
	private User user = null;
	private float significance = 0;
	private Set<Class<? extends IMapper>> mappers = new HashSet<Class<? extends IMapper>>();
	
	public Mapping(Tag tag, OntClass concept, Ontology ontology, float significance)
	{
		this.tag = tag;
		this.concept = concept;
		this.ontology = ontology;
		this.user = Configuration.getInstance().getDATABASE_TAGONTO_USER();
		this.significance = significance;
	}
	
	public Mapping(Tag tag, OntClass concept, Ontology ontology, User user, float significance)
	{
		this.tag = tag;
		this.concept = concept;
		this.ontology = ontology;
		this.user = user;
		this.significance = significance;
	}

	public OntClass getConcept()
	{
		return this.concept;
	}
	
	public Ontology getOntology()
	{
		return this.ontology;
	}

	public float getSignificance() {
		return this.significance;
	}
	
	public void setSignificance(float significance)
	{
		if (significance < 0) this.significance = 0;
		else{
			if (significance > 1) this.significance = 1;
			else this.significance = significance;
		}
	}

	public Tag getTag() 
	{
		return this.tag;
	}
	
	public User getUser()
	{
		return this.user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public String getId()
	{
		if (this.concept != null) return this.concept.getURI();
		else return "";
	}
	
	/**
	 * Gets the set of mappers which generated this mapping.
	 * @return
	 */
	public Set<Class<? extends IMapper>> getMappers()
	{
		return new HashSet<Class<? extends IMapper>>(this.mappers);
	}
	
	public void addMapper(IMapper mapper)
	{
		this.mappers.add(mapper.getClass());
	}
	
	public void addMapper(Class<? extends IMapper> mapper)
	{
		this.mappers.add(mapper);
	}
	
	public void removeMapper(IMapper mapper)
	{
		this.mappers.remove(mapper.getClass());
	}
	
	public void removeMapper(Class<? extends IMapper> mapper)
	{
		this.mappers.remove(mapper);
	}

	@Override
	public String toString() 
	{
		String rep = this.tag.getTag();
		rep += "->";
		rep += this.concept.getURI();
		rep += " S=" + Float.toString(this.significance);
		
		return rep;
	}

	public int compareTo(Object arg0) 
	{
		if (!(arg0 instanceof Mapping)) return 0;
		Mapping specMap = (Mapping) arg0;
		if (this.significance < specMap.significance) return -1;
		if (this.significance > specMap.significance) return 1;
		
		return 0;
	}
	
	public static void setUser(Collection<Mapping> mappings, User user)
	{
		for(Mapping mapping : mappings){
			mapping.user = user;
		}
	}
	
	public static void addMapper(Collection<Mapping> mappings, IMapper mapper)
	{
		for(Mapping mapping : mappings){
			mapping.addMapper(mapper);
		}
	}
}
