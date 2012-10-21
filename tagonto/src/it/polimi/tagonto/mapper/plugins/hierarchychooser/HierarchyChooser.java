package it.polimi.tagonto.mapper.plugins.hierarchychooser;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.plugins.IMatchPlugin;
import it.polimi.tagonto.mapper.plugins.PluginException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;

/**
 * This plugin searches for hierarchy relationships among candidate mappings exploring them in pairs.<br>
 * Given a pair containing two candidates (say A and B), the following could happen :
 * <ul>
 * <li> (A is a superclass of B) or (B is a superclass of A) : nothing is done.
 * <li> (A is not a superclass of B) and (B is not a superclass of A) and (A and B have a superclass in common) : 
 * 		a new possible mapping is added to the list, mapping the tag onto the common superclass.
 * <li> A and B are not correlated at all : nothing is done.
 * </ul>
 * 
 * @author Mauro Luigi Drago
 *
 */
public class HierarchyChooser implements IMatchPlugin 
{

	private boolean isInited = false;
	private Ontology ont = null;
	private Tag tag = null;
	private Collection<Mapping> mappings = null;
	
	public void init() throws PluginException 
	{
		if (this.ont == null) throw new PluginException(HierarchyChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_ONT_NOT_SET));
		if (this.tag == null) throw new PluginException(HierarchyChooser.class, 
				String.format(Resources.MSG_PLUGIN_NOT_INITED, Resources.MSG_PLUGIN_TAG_NOT_SET));
		if (this.mappings == null) throw new PluginException(HierarchyChooser.class, 
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
		if (this.isInited == false) throw new PluginException(HierarchyChooser.class,
				String.format(Resources.MSG_PLUGIN_NOT_INITED, ""));
		
		// create a model with reasoning support
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
	    spec.setReasoner(reasoner);
	    OntModel infModel = ModelFactory.createOntologyModel(spec, this.ont.getModel());
	    
	    // then calculate superclasses for each class
	    Map<OntClass,Collection<OntClass>> superClassesMap = new HashMap<OntClass,Collection<OntClass>>();
	    for(Mapping candidate : this.mappings){
	    	Iterator it = candidate.getConcept().listSuperClasses();
	    	Collection<OntClass> superClasses = new Vector<OntClass>();
	    	while(it.hasNext()){
	    		OntClass clazz = (OntClass) it.next();
	    		superClasses.add(clazz);
	    	}
	    	superClassesMap.put(candidate.getConcept(), superClasses);
	    }
	    
	    // now search for hierarchy relationships
	    Collection<Mapping> toBeAdded = new Vector<Mapping>(); // new mappings found
	    Set<OntClass> blackList = new HashSet<OntClass>();
	    for(OntClass currentClass : superClassesMap.keySet()){
	    	Collection<OntClass> superClasses = superClassesMap.get(currentClass);
	    	for(OntClass superClass : superClasses){
	    		if (superClassesMap.containsKey(superClass)){ // a superclass of the current class is between candidate mappings
	    			// increment the superclass mapping significance and decrement the subclass mapping significance
	    			for(Mapping mapping : this.mappings){
	    				if (mapping.getConcept().equals(superClass)){
	    					//TODO implement me
	    				}
	    				if (mapping.getConcept().equals(currentClass)){
	    					// TODO implement me
	    				}
	    			}
	    		}
	    	}
	    }
	   
		return null;
	}

}
