package it.polimi.tagonto;

import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;
import it.polimi.tagonto.port.restlet.OntologyExplorerRestlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
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
 
import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFProperty;
import edu.stanford.smi.protegex.owl.ProtegeOWL;

public class test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String ontId = "2";
		String conceptUri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Zinfandel";
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
		
	 
	    try {
			OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(ontUri);
		    OWLNamedClass destinationClass = owlModel.getOWLNamedClass("CabernetSauvignon");
		    OWLObjectProperty madefrom = owlModel.getOWLObjectProperty("madeFromGrape");
		    Set<DefaultRDFProperty> a = destinationClass.getAssociatedProperties();
		    Collection<DefaultRDFProperty> a7 =  destinationClass.getPossibleRDFProperties();
		    
		    Collection a6 = destinationClass.getReachableSimpleInstances();
		    
		    for(DefaultRDFProperty prop : a) {
		    	printIterator(prop.getDomains(false).iterator(), "Domains for property: "+prop.getName());
		    	printIterator(prop.getRanges(false).iterator(), "Ranges for property: "+prop.getName());
		    }
		    
		    Collection za = madefrom.getAllowedValues();
		    RDFSClass bz = madefrom.getDomain(true);
		    RDFSClass cz = madefrom.getDomain(false);
		    Collection uz = madefrom.getDomains(true);
		    Collection zz = madefrom.getDomains(false);
		    Collection zb = madefrom.getRanges(true);
		    Collection zc = madefrom.getRanges(false);
		    
		    //DefaultOWLNamedClass test = (DefaultOWLNamedClass) getAllValuesFrom(destinationClass, madefrom);
			//Set myset = new HashSet();
		    //test.getNestedNamedClasses(myset);
			System.out.println("ok");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

    public static RDFResource getAllValuesFrom(OWLNamedClass cls, RDFProperty property) {
        Collection restrictions = cls.getRestrictions(property, true);
        for (Iterator it = restrictions.iterator(); it.hasNext();) {
            OWLRestriction restriction = (OWLRestriction) it.next();
            if(restriction instanceof OWLAllValuesFrom) {
                return ((OWLAllValuesFrom)restriction).getAllValuesFrom();
            }
            if(restriction instanceof OWLHasValue) {
                return (RDFResource) ((OWLHasValue)restriction).getHasValue();
            }
        }
        return property.getRange();
    }
    
}
