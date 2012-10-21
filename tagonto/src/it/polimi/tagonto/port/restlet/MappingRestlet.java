package it.polimi.tagonto.port.restlet;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.MapperException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;
import it.polimi.tagonto.mapper.StandardMapper;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;
import it.polimi.tagonto.mapper.caching.UserCacher;

import java.io.IOException;
import java.util.Collection;

import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MappingRestlet extends Restlet 
{
	@Override
	public void handle(Request request, Response response) 
	{
		Form query = request.getEntityAsForm();
		String ontId = (String) request.getAttributes().get("ontology");
		String tag = query.getFirstValue("tag", true);
		String userIdRep = query.getFirstValue("userId", true);
		String doGreedyMatchRep = query.getFirstValue("doGreedyMatch", true);
		
		if (tag == null || userIdRep == null || ontId == null){
			response.setEntity(MappingRestlet.createErrorResponse(new Exception("Missing one or more fields in request.")));
			return;
		}
		
		OntologyPool pool = OntologyPool.getPool();
		Ontology ont = null;
		try {
			ont = pool.getOntology(Integer.parseInt(ontId));
			if (ont == null){
				response.setEntity(MappingRestlet.createErrorResponse(new Exception("Invalid ontology id, no ontology with that id exists.")));
				return;
			} 
		} catch (NumberFormatException e) {
			response.setEntity(MappingRestlet.createErrorResponse(new Exception("Invalid format for ontology id.")));
			return;
		}
		
		User user = null;
		try{
			Integer userId = -1;
			userId = Integer.parseInt(userIdRep);
			
			user = UserCacher.getInstance().getCachedUser(userId);
			if (user == null) throw new TagontoException("No user with the specified id exists.");
		}catch(NumberFormatException e){
			response.setEntity(MappingRestlet.createErrorResponse(new Exception("Invalid format for user Id.")));
			return;
		} catch (TagontoException e) {
			response.setEntity(MappingRestlet.createErrorResponse(e));
			return;
		}
		
		try {
			StandardMapper mapper = new StandardMapper();
			mapper.setTag(new Tag(tag));
			mapper.setontology(ont);
			mapper.setUser(user);
			
			boolean doGreedyMatch = false;
			if (doGreedyMatchRep != null){
				doGreedyMatch = Boolean.parseBoolean(doGreedyMatchRep);
			}
			mapper.setGreedyMatch(doGreedyMatch);
			mapper.init();
			response.setEntity(MappingRestlet.createMappingsRep(mapper.run(), ont));
		} catch (MapperException e) {
			response.setEntity(MappingRestlet.createErrorResponse(e));
		}
	}
	
	private static DomRepresentation createMappingsRep(Collection<Mapping> mappings, Ontology ontology)
	{
		DomRepresentation result = null;
		try {
			result = new DomRepresentation(MediaType.APPLICATION_XML);
			Document doc = result.getDocument();
			
			Element rootElement = doc.createElement("tagonto");
			doc.appendChild(rootElement);
			
			rootElement.setAttribute("requestSatisfied", "true");

			Element mappingsElement = doc.createElement("mappings");
			rootElement.appendChild(mappingsElement);
			
			if (ontology == null) return result; 
			mappingsElement.setAttribute("ontologyId", new Integer(ontology.getId()).toString());
			mappingsElement.setAttribute("ontologyUri", ontology.getUri());
			
			if (mappings == null) return result;
			for(Mapping mapping : mappings){
				Element mappingElement = doc.createElement("mapping");
				mappingsElement.appendChild(mappingElement);
				
				mappingElement.setAttribute("tag", mapping.getTag().getTag());
				mappingElement.setAttribute("conceptUri", mapping.getConcept().getURI());
				mappingElement.setAttribute("concept", mapping.getConcept().getLocalName());
				mappingElement.setAttribute("significance", Float.toString(mapping.getSignificance()));
			}
		} catch (IOException e) {
			//TODO write me
		}
		return result;
	}
	
	private static DomRepresentation createErrorResponse(Exception exception)
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
