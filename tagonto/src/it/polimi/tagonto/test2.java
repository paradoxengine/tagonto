package it.polimi.tagonto;

import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;
import it.polimi.tagonto.port.restlet.OntologyExplorerRestlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.PelletLoader;
import org.mindswap.pellet.owlapi.Reasoner;
import org.mindswap.pellet.utils.ATermUtils;
import org.mindswap.pellet.utils.URIUtils;
import org.restlet.data.Form;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import aterm.ATermAppl;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.impl.AllValuesFromRestrictionImpl;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

public class test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String ontId = "2";
		String conceptUri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#WhiteBordeaux";
		String propUri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#madeFromGrape";
		String ontUri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine";
		
		String configFile = null;
		configFile = "config/tagonto_config.prop";
		
		try {
			Configuration.createConfiguration(configFile);
		} catch (TagontoException e){ 
			e.printStackTrace();
			return;
		}
		
        OntModel mod = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);  
        mod.read( ontUri );
		
        OntClass myclass = mod.getOntClass(conceptUri);
        
        for (ExtendedIterator i= myclass.listSuperClasses(false); i.hasNext(); ) {
	              OntClass superClass = (OntClass)i.next();
	              if (superClass.isRestriction()) {
	                Restriction restriction = superClass.asRestriction();
	                if (restriction.isAllValuesFromRestriction()) {   //the value of the restriction is allvaluesfrom
	                  AllValuesFromRestriction allvaluesR = restriction.asAllValuesFromRestriction();
	                   Resource r = allvaluesR.getAllValuesFrom();
	                   
	                  /**************************************
	                      I know that the restriction for workable property for motherboard class is Processor and RAM
	                      But how to continue from here to get the class for Processor and RAM?
	                  *************************************/

	                }
	              }
	              
           }
	}

    public static void printIterator(Iterator i, String header) {
        System.out.println(header);
        for(int c = 0; c < header.length(); c++)
            System.out.print("=");
        System.out.println();
        
        if(i.hasNext()) {
	        while (i.hasNext()) 
	            System.out.println( i.next() );
        }       
        else
            System.out.println("<EMPTY>");
        
        System.out.println();
    }
	
}
