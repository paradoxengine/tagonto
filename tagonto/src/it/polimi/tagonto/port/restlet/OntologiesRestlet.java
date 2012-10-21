package it.polimi.tagonto.port.restlet;

import java.io.IOException;

import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;

import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OntologiesRestlet extends Restlet
{
	@Override
	public void handle(Request request, Response response) 
	{
		Form query = request.getEntityAsForm();
		String URI = query.getFirstValue("URI", true);
		
		if (URI == null){
			response.setEntity(OntologiesRestlet.createErrorResponse(new Exception("Missing one or more fields in request.")));
			return;
		}
		
		OntologyPool pool = OntologyPool.getPool();
		Ontology ont = null;
		try {
			ont = pool.getOntology(URI);
			response.setEntity(OntologyResource.getDomRepresentation(ont));
		} catch (TagontoException e) {
			response.setEntity(OntologiesRestlet.createErrorResponse(e));
		}
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

	