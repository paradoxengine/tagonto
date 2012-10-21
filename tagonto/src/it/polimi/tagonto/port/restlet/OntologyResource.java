package it.polimi.tagonto.port.restlet;

import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;

import java.io.IOException;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class OntologyResource extends Resource 
{
	private Ontology ontology = null;
	private int id = -1;
	
	public OntologyResource(Context context, Request request, Response response)
	{
		super(context, request, response);
		
		this.id = Integer.parseInt((String) request.getAttributes().get("ontology")); 
		
		this.getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	@Override
	public Representation getRepresentation(Variant variant) 
	{
		Representation result = null;
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)){
			result = OntologyResource.getDomRepresentation(this.ontology);
		}	
		return result;
	}

	@Override
	public boolean allowDelete()
	{
		return false;
	}

	@Override
	public boolean allowPost()
	{
		return false;
	}

	@Override
	public boolean allowPut()
	{
		return false;
	}

	@Override
	public boolean allowGet() 
	{
		return true;
	}
	
	
	@Override
	public void handleGet() 
	{
		OntologyPool pool = OntologyPool.getPool();
		this.ontology = pool.getOntology(this.id);
		
		super.handleGet();
	}

	static DomRepresentation getDomRepresentation(Ontology ontology)
	{
		DomRepresentation result = null;
		try {
			result = new DomRepresentation(MediaType.APPLICATION_XML);
			Document doc = result.getDocument();
			
			Element rootElement = doc.createElement("tagonto");
			doc.appendChild(rootElement);
			
			rootElement.setAttribute("requestSatisfied", "true");
			
			if (ontology != null){
				Element ontologyElement = doc.createElement("ontology");
				rootElement.appendChild(ontologyElement);
				
				Element ontologyUri = doc.createElement("Uri");
				Text uriText = doc.createTextNode(ontology.getUri());
				ontologyUri.appendChild(uriText);
				ontologyElement.appendChild(ontologyUri);
				
				Element ontologyId = doc.createElement("id");
				Text idText = doc.createTextNode(new Integer(ontology.getId()).toString());
				ontologyId.appendChild(idText);
				ontologyElement.appendChild(ontologyId);
			}
		} catch (IOException e) {
			//TODO write me
		}
		return result;
	}
	
}
