package it.polimi.tagonto.mapper.caching;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;

import java.util.Collection;
import java.util.Iterator;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.AlreadyExistsException;

/**
 * The Rdf mapping cacher stores mapping using the Jena Rdb facility, storing the ontology
 * model representing mappings onto a db.
 * 
 * @author Mauro Luigi Drago 
 *
 */
public class RdfMappingCacher implements IMappingCacher 
{
	private static final RdfMappingCacher instance = new RdfMappingCacher();
	
	private Configuration config = Configuration.getInstance();
	private OntModel ontModel = null;
	
	private OntClass mappingConcept = null;
	private OntClass tagConcept = null;
	private OntClass mapperConcept = null;
	
	private OntProperty hasSignificanceProperty = null;
	private OntProperty mappedByProperty = null;
	private OntProperty onTagProperty = null;
	private OntProperty onConceptProperty = null;
	
	private RdfMappingCacher()
	{
		try {
            Class.forName( this.config.getRDF_CACHER_DB_DRIVER());
        }
        catch (Exception e) {
            System.err.println( "Failed to load the driver for the database: " + e.getMessage() );
            System.err.println( "Have you got the CLASSPATH set correctly?" );
        }
		
		IDBConnection conn = new DBConnection(config.getRDF_CACHER_JDBC_URL(), 
				config.getRDF_CACHER_DB_USER(), 
				config.getRDF_CACHER_DB_PASS(), 
				config.getRDF_CACHER_DB_TYPE());
		
		ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
		
		boolean justCreated = true;
		Model baseModel = null;
		try{
			baseModel = maker.createModel(config.getRDF_CACHER_TAG_ONTOLOGY_URI(), true);
		}catch(AlreadyExistsException e){
			baseModel = maker.createModel(config.getRDF_CACHER_TAG_ONTOLOGY_URI(), false);
			justCreated = false;
		}
		
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
        spec.setImportModelMaker(maker);
        
		this.ontModel = ModelFactory.createOntologyModel(spec, baseModel); 
		
		if (justCreated == true) this.ontModel.read(config.getRDF_CACHER_TAG_ONTOLOGY_URI());
		
		this.mappingConcept = this.ontModel.getOntClass("http://polimi.it/tagonto/tagonto.owl#Mapping");
		this.tagConcept = this.ontModel.getOntClass("http://polimi.it/tagonto/tagonto.owl#Tag");
		this.mapperConcept = this.ontModel.getOntClass("http://polimi.it/tagonto/tagonto.owl#Mapper");
		
		this.hasSignificanceProperty = this.ontModel.getOntProperty("http://polimi.it/tagonto/tagonto.owl#hasSignificance");
		this.mappedByProperty = this.ontModel.getOntProperty("http://polimi.it/tagonto/tagonto.owl#mappedBy");
		this.onTagProperty = this.ontModel.getOntProperty("http://polimi.it/tagonto/tagonto.owl#onTag");
		this.onConceptProperty = this.ontModel.getOntProperty("http://polimi.it/tagonto/tagonto.owl#onConcept");
	}
	
	public static RdfMappingCacher getInstance()
	{
		return RdfMappingCacher.instance;
	}
	
	public void cacheMapping(Mapping mapping)
		throws TagontoException 
	{
		// first check if the mapping already exists
		String mappingId = "tagonto-mapping/" + mapping.getTag().getTag() + "-" + mapping.getUser().getName() + "-" + mapping.getId();
		Individual mappingIndividual = this.ontModel.getIndividual(mappingId);
		if (mappingIndividual == null){
			mappingIndividual = this.mappingConcept.createIndividual(mappingId);
			
			// TODO review this 2 following lines
			Individual mapperIndividual = this.mapperConcept.createIndividual(mapping.getUser().getName());
			Individual tagIndividual = this.tagConcept.createIndividual(mapping.getTag().getTag());
			
			this.ontModel.add(mappingIndividual, this.hasSignificanceProperty, Float.toString(mapping.getSignificance()));
			this.ontModel.add(mappingIndividual, this.mappedByProperty, mapperIndividual);
			this.ontModel.add(mappingIndividual, this.onTagProperty, tagIndividual);
			this.ontModel.add(mappingIndividual, this.onConceptProperty, "http://stupid_uri");
		}else{ // we just have to update significance
			Statement significanceStm = mappingIndividual.getProperty(this.hasSignificanceProperty);
			this.ontModel.remove(significanceStm);
			this.ontModel.add(mappingIndividual, this.hasSignificanceProperty, Float.toString(mapping.getSignificance()));
		}
	}

	public void cacheMappings(Collection<Mapping> mappings) 
		throws TagontoException 
	{
		for(Mapping mapping : mappings){
			this.cacheMapping(mapping);
		}
	}

	/**
	 * Always returns null, rdf search is too much slow
	 */
	public Collection<Mapping> getCachedMappings(Tag tag, Ontology ontology, User user) throws TagontoException 
	{
		return null;
	}

	/**
	 * Always returns null, rdf search is too much slow
	 */
	public Collection<Mapping> getAggregatedCachedMappings(Tag tag, Ontology ontology) throws TagontoException 
	{
		return null;
	}
	
	public void test2()
	{
		OntClass mappingConcept = this.ontModel.getOntClass("http://polimi.it/tagonto/tagonto.owl#Mapping");
		Iterator it = mappingConcept.listInstances();
		while(it.hasNext()){
			Resource res = (Resource)it.next();
			Iterator it2 = res.listProperties();
			while(it2.hasNext()){
				System.out.println(it2.next());
			}
		}
	}
}
