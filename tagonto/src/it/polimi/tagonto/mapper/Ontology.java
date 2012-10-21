package it.polimi.tagonto.mapper;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.caching.OntologyCacher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * A proxy for an ontology.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class Ontology 
{
	private int id = -1;
	private String uri = null;
	private OntModel model = null;
	
	private Map<String, Collection<OntClass>> namedConcepts = null;
	private Collection<OntClass> cachedConceptCollection = null;
	
	private Map<String,PropertyStat> propertyStats = null;
	
	public static class PropertyStat
	{
		private OntClass firstConcept = null;
		private OntClass secondConcept = null;
		private int count = 0;
		
		private PropertyStat(OntClass firstConcept, OntClass secondConcept, int count)
		{
			this.firstConcept = firstConcept;
			this.secondConcept = secondConcept;
			this.count = count;
		}
		
		public OntClass getFirstConcept()
		{
			return this.firstConcept;
		}
		
		public OntClass getSecondConcept()
		{
			return this.secondConcept;
		}
		
		public int getCount()
		{
			return this.count;
		}
		
		private void setCount(int count)
		{
			this.count = count;
		}
		
		public String getId()
		{
			return this.firstConcept.getURI() + "-" + this.secondConcept.getURI();
		}

		@Override
		public int hashCode() 
		{
			return this.getId().hashCode();
		}
	}
	
	public Ontology(String uri) throws TagontoException
	{
		this.uri = uri;
		/*Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
	    spec.setReasoner(reasoner);
	    this.infModel = ModelFactory.createOntologyModel(spec, this.model); 
	    */
		this.model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		try{
			this.model.read(this.uri);
		}catch(Exception e){
			throw new TagontoException(e);
		}
		
		OntologyCacher cacher = OntologyCacher.getInstance();
		if (cacher.loadOntology(this) == true) return; // already cached
		else{ // needs to be cached
			cacher.cacheOntology(this);
		}
	}
	
	/**
	 * Gets the URI of this ontology.
	 * 
	 * @return the URI of the ontology.
	 */
	public String getUri()
	{
		return this.uri;
	}
	
	/**
	 * Gets the ontology model of this ontology.
	 * 
	 * @return the ontology model.
	 */
	public OntModel getModel()
	{
		return this.model;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Gets the list of all named concepts defined in the ontology.
	 * 
	 * @return the list of all named concepts defined in the ontology.
	 */
	public Collection<OntClass> getNamedConcepts()
	{
		if (this.namedConcepts == null){
			this.namedConcepts = new HashMap<String,Collection<OntClass>>(); 
			this.cachedConceptCollection = new Vector<OntClass>();
			
			Iterator it = this.model.listNamedClasses();
			while(it.hasNext() == true){
				OntClass concept = (OntClass) it.next();
				if (this.namedConcepts.containsKey(concept.getLocalName())){
					Collection<OntClass> classes = this.namedConcepts.get(concept.getLocalName());
					classes.add(concept);
				}else{
					Collection<OntClass> classes = new Vector<OntClass>();
					classes.add(concept);
					this.namedConcepts.put(concept.getLocalName(), classes);
				}/*
				if (!concept.getURI().equals("http://www.w3.org/2002/07/owl#Nothing") 
				    &&
				    !concept.getURI().equals("http://www.w3.org/2002/07/owl#Thing"))
				{
					this.cachedConceptCollection.add(concept);
				}*/
				this.cachedConceptCollection.add(concept);
			}
			//this.namedConcepts.remove("http://www.w3.org/2002/07/owl#Nothing");
			//this.namedConcepts.remove("http://www.w3.org/2002/07/owl#Thing");
		}
		return this.cachedConceptCollection;
	}
	
	/**
	 * Gets the list of all named concepts defined in the ontology having the specified local name.
	 * 
	 * @param localName the local name of the concepts to retrieve. 
	 * @return the list of all named concepts defined in the ontology with the specified local name.
	 */
	public Collection<OntClass> getNamedConcepts(String localName)
	{
		if (this.namedConcepts == null) this.getNamedConcepts();
		if (this.namedConcepts.containsKey(localName)) return this.namedConcepts.get(localName);
		else return new Vector<OntClass>();
	}
	
	public OntClass getConcept(String URI)
	{
		return this.model.getOntClass(URI);
	}
	
	public Map<String,PropertyStat> getPropertyStats()
	{
		if (this.propertyStats == null) this.initPropertyStats();
		
		return this.propertyStats;
	}
	
	public PropertyStat getPropertyStat(OntClass firstConcept, OntClass secondConcept)
	{	
		if (this.propertyStats == null) this.initPropertyStats();
		
		PropertyStat shadow = new PropertyStat(firstConcept, secondConcept, 0);
		return this.propertyStats.get(shadow.getId());
	}
	
	private void initPropertyStats()
	{
		if (this.propertyStats == null){
			this.propertyStats = new HashMap<String,PropertyStat>();
			
			Collection<OntClass> classes = this.getNamedConcepts();
			
			Map<Property, Collection<OntClass>> propertyScopes = new HashMap<Property,Collection<OntClass>>();
			
			for(OntClass clazz : classes){
				Iterator it = clazz.listDeclaredProperties();
				while(it.hasNext()){
					Property prop = (Property) it.next();
					Collection<OntClass> propertyScope = propertyScopes.get(prop);
					if (propertyScope == null){
						propertyScope = new HashSet<OntClass>();
						propertyScopes.put(prop, propertyScope);
					}
					propertyScope.add(clazz);
				}
			}
			
			for(Collection<OntClass> scope : propertyScopes.values()){
				int i = 0;
				int j = 0;
				OntClass[] array = scope.toArray(new OntClass[0]);
				
				for(i = 0; i < array.length; i++){
					OntClass class1 = array[i];
					
					for(j = i+1; j < array.length; j++){
						OntClass class2 = array[j];
						boolean alreadyPresent = false;
						PropertyStat stat = new PropertyStat(class1, class2, 0);
						if (this.propertyStats.containsKey(stat.getId())){
							stat = this.propertyStats.get(stat.getId());
							alreadyPresent = true;
						}
						
						stat.setCount(stat.getCount() + 1);
						
						if (alreadyPresent == false) this.propertyStats.put(stat.getId(), stat);
					}
				}
			}
		}
	}
}
