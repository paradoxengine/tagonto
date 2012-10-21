package it.polimi.tagonto.port.restlet;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;
import it.polimi.tagonto.mapper.caching.CompositeMappingCacher;
import it.polimi.tagonto.mapper.caching.UserCacher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.Role;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import org.mindswap.pellet.utils.ATermUtils;
import org.mindswap.pellet.utils.URIUtils;

import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import aterm.ATermAppl;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntologyExplorerRestlet extends Restlet 
{
	@Override
	public void handle(Request request, Response response) 
	{
		//Reference resourceRef = request.getResourceRef();
		Form query = request.getEntityAsForm();
		String ontId = (String) request.getAttributes().get("ontology");
//		String tag = query.getFirstValue("tag", true);
//		String userIdRep = query.getFirstValue("userId", true);
		String conceptUri = query.getFirstValue("conceptUri");
//		String significanceRep = query.getFirstValue("significance");
		
		if (conceptUri == null){
			response.setEntity(OntologyExplorerRestlet.createResponse(new Exception("Missing one or more fields in request.")));
			return;
		}
		

		OntologyPool pool = OntologyPool.getPool();
		Ontology ont = null;
		try {
			ont = pool.getOntology(Integer.parseInt(ontId));
			if (ont == null){
				response.setEntity(OntologyExplorerRestlet.createResponse(new Exception("Invalid ontology id, no ontology with that id exists.")));
				return;
			} 
		} catch (NumberFormatException e) {
			response.setEntity(OntologyExplorerRestlet.createResponse(new Exception("Invalid format for ontology id.")));
			return;
		}
		/*
		float significance = 0f;
		try{
			significance = Float.parseFloat(significanceRep);
		}catch(NumberFormatException e){
			response.setEntity(OntologyExplorerRestlet.createResponse(new Exception("Invalid format for significance.")));
			return;
		}
		*/
		OntClass concept = ont.getConcept(conceptUri);
		if (concept == null){
			response.setEntity(OntologyExplorerRestlet.createResponse(new Exception("No concept with the specified URI exists.")));
			return;
		}
		
		OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		try{
			model.read(ont.getUri());
		}catch(Exception e){
			response.setEntity(OntologyExplorerRestlet.createResponse(new Exception("Invalid ontology in creating model")));
			return;
		}
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		// create the Pellet reasoner
		Reasoner reasoner = new Reasoner( manager );


		try {
			OWLOntology ontologia = manager.loadOntology( URI.create( ont.getUri() ) );
			reasoner.loadOntology( ontologia );
			KnowledgeBase kb = reasoner.getKB();
			kb.realize();
			OWLClass clazz = manager.getOWLDataFactory().getOWLClass(new URI(concept.getURI()));
//	        ATermAppl amy = ATermUtils.makeTermAppl(concept.getURI());
//			URI PersonURI = URI.create( concept.getURI() );
//			OWLIndividual Person = manager.getOWLDataFactory().getOWLIndividual( PersonURI );   
//			Map instances  = reasoner.getDataPropertyRelationships(Person);
//	        Set<ATermAppl> piffo = reasoner.getKB().getProperties();
	        
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
	                        if (URIUtils.getLocalName(((OWLClass)od).getURI()).equals(URIUtils.getLocalName(clazz.getURI())))
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
		
		
       // printIterator(concept.listSuperClasses(), "All super classes of " + concept.getLocalName());
        // OntClass provides function to print *only* the direct subclasses 
      //  printIterator(concept.listSuperClasses(true), "Direct superclasses of " + concept.getLocalName());
        
      //  System.out.println();
		return;
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
    
	private static DomRepresentation createResponse(Exception exception)
	{
		DomRepresentation result = null;
		try {
			result = new DomRepresentation(MediaType.APPLICATION_XML);
			Document doc = result.getDocument();
			
			Element rootElement = doc.createElement("tagonto");
			doc.appendChild(rootElement);
			
			if (exception == null) rootElement.setAttribute("requestSatisfied", "true");
			else{
				rootElement.setAttribute("requestSatisfied", "false");
				
				Element exceptionElement = doc.createElement("exception");
				rootElement.appendChild(exceptionElement);
				exceptionElement.setAttribute("type", exception.getClass().getCanonicalName());
				exceptionElement.setTextContent(exception.getMessage());
			}
		} catch (IOException e) {
			// we cannot do anything, let the webserver show internal server error
		}
		return result;
	}
}
