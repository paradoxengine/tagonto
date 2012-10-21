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

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
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

public class tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String ontId = "2";
		String conceptUri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#PinotBlanc";
		String propUri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#madeFromGrape";
		
		String configFile = null;
		configFile = "config/tagonto_config.prop";
		
		try {
			Configuration.createConfiguration(configFile);
		} catch (TagontoException e){ 
			e.printStackTrace();
			return;
		}
  	    // create Pellet reasoner
        com.hp.hpl.jena.reasoner.Reasoner reasoner = PelletReasonerFactory.theInstance().create();
        
        // create an empty model
        Model emptyModel = ModelFactory.createDefaultModel( );
        
        // create an inferencing model using Pellet reasoner
        InfModel model = ModelFactory.createInfModel( reasoner, emptyModel );
        OntModel mod = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);  
        // read the file
        mod.read( "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine" );
        
        
        // print superclasses
        Resource c = mod.getResource( conceptUri );
		Property pro = mod.getProperty(propUri);
		
		OntClass conc = mod.getOntClass(conceptUri);
		
		//printIterator(conc.listDeclaredProperties(),"Properties declared by: "+conc.getLocalName());
		

       //printIterator(mod.limod.listSubjectsWithProperty(pro)stObjectsOfProperty(pro ), "Oggetti di" + pro.getLocalName());
      // printIterator(mod.listSubjectsWithProperty(pro), "Soggetti di " + pro.getLocalName());
        ResIterator it = mod.listSubjectsWithProperty(pro);
       while(it.hasNext()) {
 
    	  Resource res = it.nextResource();
    	  printIterator(res.listProperties()," Proprieta di " + res.getLocalName());
    	
       }
  //    printIterator(mod.listObjectsOfProperty(mod.listSubjectsWithProperty(pro).nextResource(),pro),"TEST12");
        /*
		try {
			Configuration.createConfiguration(configFile);
		} catch (TagontoException e){ 
			e.printStackTrace();
			return;
		}
	
		OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		
		try{
			model.read("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine");
		}catch(Exception e){
			return;
		}
		OntClass c = model.getOntClass( conceptUri );
		OntProperty pro = model.getOntProperty(propUri);
		
		ExtendedIterator declaringClasses = pro.listDeclaringClasses(false);
		while(declaringClasses.hasNext()) {
			if( declaringClasses.next().equals(c))
					System.out.println("FOUND");
			
		}
		
		ExtendedIterator declaringPrope = c.listDeclaredProperties(false);
		
		while(declaringPrope.hasNext()) {
			if(declaringPrope.next().equals(pro)) 
					System.out.println("REALLY FOUND");
		}
		
		/*
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		// create the Pellet reasoner
		Reasoner reasoner = new Reasoner( manager );


		try {

			OWLOntology ontologia = manager.loadOntology( URI.create("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine") );
			reasoner.loadOntology( ontologia );
			KnowledgeBase kb = reasoner.getKB();
			kb.realize();
			OWLClass clazz = manager.getOWLDataFactory().getOWLClass(new URI(conceptUri));
			OWLObjectProperty prop = manager.getOWLDataFactory().getOWLObjectProperty(new URI(propUri));
			
			
		//	clazz.getSubClasses(ontologia);
		//	Set<OWLDescription> superclassi = clazz.getSuperClasses(ontologia);
	//		Set<Set<OWLClass>> subclasses = reasoner.getSuperClasses(clazz);
		
			//Set<OWLDescription> subclass2 = clazz.getSuperClasses(ontologia);
			// Set<ATermAppl> pippo = kb.getClasses();
			 
			
			PelletLoader loader = new PelletLoader(kb);
			// http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Anjou
		ATermAppl property = loader.term(prop);
			ATermAppl owlclass = loader.term(clazz);
			System.out.println("WOW");

			List<ATermAppl> test = kb.getObjectPropertyValues(property,owlclass );
			
			
//	        ATermAppl amy = ATermUtils.makeTermAppl(concept.getURI());
//			URI PersonURI = URI.create( concept.getURI() );
//			OWLIndividual Person = manager.getOWLDataFactory().getOWLIndividual( PersonURI );   
//			Map instances  = reasoner.getDataPropertyRelationships(Person);
//	        Set<ATermAppl> piffo = reasoner.getKB().getProperties();
	        
			
			/*
	        Set<OWLObjectProperty> classProps = new HashSet<OWLObjectProperty>();
	        Set<OWLObjectProperty> props = reasoner.getObjectProperties();
	        
	        for (OWLObjectProperty prop : props)
	        {
	            if (!prop.getDomains(ontologia).isEmpty())
	            {
	                for (OWLDescription od : prop.getDomains(ontologia))
	                {
	                    if (od != null && od instanceof OWLClass)
	                    {
	                    	//TODO
	                    	if(subclasses.iterator().next().contains(od) || 
	                    		(URIUtils.getLocalName(((OWLClass)od).getURI()).equals(URIUtils.getLocalName(clazz.getURI()))))
	                        {
	                            classProps.add(prop);
	                        }
	                    }
	                }
	            }
	        }
	        	System.out.println("wow");
	        
			//Set<Role> puffo = reasoner.getKB().getPossibleProperties(amy);
			//System.out.println(puffo);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		

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
