package it.polimi.tagonto.port.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

public class TagontoApplication extends Application 
{
	@Override
	public Restlet createRoot() 
	{
		Router router = new Router(this.getContext());
		
		// create the ontologies restlet
		OntologiesRestlet ontologiesRestlet = new OntologiesRestlet();
		router.attach("/ontologies/getbyuri", ontologiesRestlet);
		router.attach("/ontologies/{ontology}", OntologyResource.class);
		
		MappingRestlet mappingRestlet = new MappingRestlet();
		router.attach("/ontologies/{ontology}/map", mappingRestlet);
		
		ForceMappingRestlet forceMappingRestlet = new ForceMappingRestlet();
		router.attach("/ontologies/{ontology}/force_mapping", forceMappingRestlet);
		
		ReasoningRestlet reasoningRestlet = new ReasoningRestlet();
		router.attach("/ontologies/{ontology}/{concept}/{property}/getStatements", reasoningRestlet);
		router.attach("/ontologies/{ontology}/{concept}/getStatements", reasoningRestlet);
		
		return router;
	}
	
	

}
