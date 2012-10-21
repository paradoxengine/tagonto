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

import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.hpl.jena.ontology.OntClass;

public class ForceMappingRestlet extends Restlet 
{
	@Override
	public void handle(Request request, Response response) 
	{
		//Reference resourceRef = request.getResourceRef();
		Form query = request.getEntityAsForm();
		String ontId = (String) request.getAttributes().get("ontology");
		String tag = query.getFirstValue("tag", true);
		String userIdRep = query.getFirstValue("userId", true);
		String conceptUri = query.getFirstValue("conceptUri");
		String significanceRep = query.getFirstValue("significance");
		
		if (tag == null || userIdRep == null || conceptUri == null || significanceRep == null){
			response.setEntity(ForceMappingRestlet.createResponse(new Exception("Missing one or more fields in request.")));
			return;
		}
		
		OntologyPool pool = OntologyPool.getPool();
		Ontology ont = null;
		try {
			ont = pool.getOntology(Integer.parseInt(ontId));
			if (ont == null){
				response.setEntity(ForceMappingRestlet.createResponse(new Exception("Invalid ontology id, no ontology with that id exists.")));
				return;
			} 
		} catch (NumberFormatException e) {
			response.setEntity(ForceMappingRestlet.createResponse(new Exception("Invalid format for ontology id.")));
			return;
		}
		
		float significance = 0f;
		try{
			significance = Float.parseFloat(significanceRep);
		}catch(NumberFormatException e){
			response.setEntity(ForceMappingRestlet.createResponse(new Exception("Invalid format for significance.")));
			return;
		}
		
		OntClass concept = ont.getConcept(conceptUri);
		if (concept == null){
			response.setEntity(ForceMappingRestlet.createResponse(new Exception("No concept with the specified URI exists.")));
			return;
		}
		
		User user = null;
		try{
			Integer userId = -1;
			userId = Integer.parseInt(userIdRep);
			
			user = UserCacher.getInstance().getCachedUser(userId);
			if (user == null) throw new TagontoException("No user with the specified id exists.");
		}catch(NumberFormatException e){
			response.setEntity(ForceMappingRestlet.createResponse(new Exception("Invalid format for user Id.")));
			return;
		} catch (TagontoException e) {
			response.setEntity(ForceMappingRestlet.createResponse(e));
			return;
		}
		
		Mapping mapping = new Mapping(new Tag(tag), concept, ont, user, significance);
		try {
			CompositeMappingCacher.getInstance().cacheMapping(mapping);
			response.setEntity(ForceMappingRestlet.createResponse(null));
		} catch (TagontoException e) {
			response.setEntity(ForceMappingRestlet.createResponse(e));
			return;
		}
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
