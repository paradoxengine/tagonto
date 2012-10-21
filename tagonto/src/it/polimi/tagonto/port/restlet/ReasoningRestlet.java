package it.polimi.tagonto.port.restlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.OntologyPool;
import it.polimi.tagonto.mapper.utility.DbConnectionManager;

import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class ReasoningRestlet extends Restlet 
{
	private static class ResultEntry
	{
		private String propertyUri = null;
		private String objectUri = null;
		
		private ResultEntry(String propertyUri, String objectUri)
		{
			this.propertyUri = propertyUri;
			this.objectUri = objectUri;
		}

		public String getObjectUri() 
		{
			return objectUri;
		}

		public String getPropertyUri() 
		{
			return propertyUri;
		}
	}
	
	@Override
	public void handle(Request request, Response response) 
	{
		Integer ontologyId = null;
		Integer conceptId = null;
		Integer propertyId = null;
		Ontology ontology = null;
		String conceptUri = null;
		String propertyUri = null;
		
		Object param = request.getAttributes().get("ontology");
		if (param == null){
			response.setEntity(ReasoningRestlet.createErrorResponse(new TagontoException("No ontology id specified")));
			return;
		}else{
			try{
				ontologyId = Integer.parseInt((String)param);
			}catch(NumberFormatException e){
				response.setEntity(ReasoningRestlet.createErrorResponse(new TagontoException("Invalid ontology id specified")));
				return;
			}
		}
		
		param = request.getAttributes().get("concept");
		if (param == null){
			response.setEntity(ReasoningRestlet.createErrorResponse(new TagontoException("No concept id specified")));
			return;
		}else{
			try{
				conceptId = Integer.parseInt((String)param);
			}catch(NumberFormatException e){
				response.setEntity(ReasoningRestlet.createErrorResponse(new TagontoException("Invalid concept id specified")));
				return;
			}
		}
		
		param = request.getAttributes().get("property");
		if (param != null){
			try{
				propertyId = Integer.parseInt((String)param);
			}catch(NumberFormatException e){
				response.setEntity(ReasoningRestlet.createErrorResponse(new TagontoException("Invalid property id specified")));
				return;
			}
		}
		
		// now get URIS
		try {
			conceptUri = this.getConceptUri(conceptId);
			if (propertyId != null) propertyUri = this.getPropertyUri(propertyId);
		} catch (TagontoException e) {
			response.setEntity(ReasoningRestlet.createErrorResponse(new TagontoException("Invalid id specified")));
			return;
		} 
		
		// now ask for the reasoning
		ontology = OntologyPool.getPool().getOntology(ontologyId);
		OntModel model = ontology.getModel();
		OntClass concept = model.getOntClass(conceptUri);
		
		Collection<ResultEntry> results = new Vector<ResultEntry>();
		
		// now examine superclasses
		/*ExtendedIterator superClassesIt = concept.listSuperClasses();
		while(superClassesIt.hasNext()){
			OntClass clazz = (OntClass) superClassesIt.next();
			System.out.println(clazz.getURI());
			if (clazz.isRestriction()){
				Restriction restr = clazz.asRestriction();
				System.out.println(restr.getOnProperty().getURI());
				if (restr.isAllValuesFromRestriction()){
					AllValuesFromRestriction specificRestr = restr.asAllValuesFromRestriction();
					Resource valuesFrom = specificRestr.getAllValuesFrom();
				}
				if (restr.isSomeValuesFromRestriction()){
					SomeValuesFromRestriction specificRestr = restr.asSomeValuesFromRestriction();
					Resource valuesFrom = specificRestr.getSomeValuesFrom();
				}
			}
		}*/
		
		Resource subjectRes = model.getOntClass(conceptUri);
		Property property = null;
		Resource shadowRes = null;
		if (propertyUri != null) property = model.getProperty(propertyUri);
		
		ExtendedIterator stmIt = model.listStatements(subjectRes, property, shadowRes);
		while(stmIt.hasNext()){
			Statement stm = (Statement)stmIt.next();
			
			ResultEntry entry = new ResultEntry(stm.getPredicate().getURI(), stm.getResource().getURI());
			results.add(entry);
		}
		
		response.setEntity(ReasoningRestlet.createResponse(results));
		
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
	
	private static DomRepresentation createResponse(Collection<ResultEntry> results)
	{
		DomRepresentation result = null;
		try {
			result = new DomRepresentation(MediaType.APPLICATION_XML);
			Document doc = result.getDocument();
			
			Element rootElement = doc.createElement("tagonto");
			doc.appendChild(rootElement);
			
			rootElement.setAttribute("requestSatisfied", "true");

			Element statementsElement = doc.createElement("statements");
			rootElement.appendChild(statementsElement);
			
			for(ResultEntry entry : results){
				Element statementElement = doc.createElement("statement");
				statementElement.setAttribute("property", entry.getPropertyUri());
				statementElement.setAttribute("object", entry.getObjectUri());
				
				statementsElement.appendChild(statementElement);
			}
		} catch (IOException e) {
			//TODO write me
		}
		return result;
	}
	
	private String getConceptUri(Integer conceptId) throws TagontoException
	{
		DbConnectionManager dbMgr = DbConnectionManager.getInstance();
		Connection conn = null;
		
		try{
			conn = dbMgr.getConnection();
			
			PreparedStatement stm = null;
			ResultSet result = null;
			try{
				stm = conn.prepareStatement("SELECT name FROM ontology_concepts WHERE id=?");
				stm.setInt(1, conceptId);
				result = stm.executeQuery();
				
				if (result.first() == false) throw new TagontoException("result set empty.");
				return result.getString("name");
				
			}finally{
				Exception exception = null;
				try {
					if (result != null) result.close();
				} catch (SQLException e) { exception = e;}
				
				try {
					if (stm != null) stm.close();
				} catch (SQLException e) { exception = e;}
				
				if (exception != null) throw new TagontoException("Unable to get concept uri", exception);
			}
			
		} catch (SQLException e) {
			throw new TagontoException("Unable to get concept uri :", e);
		} finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private String getPropertyUri(Integer propertyId) throws TagontoException
	{
		DbConnectionManager dbMgr = DbConnectionManager.getInstance();
		Connection conn = null;
		
		try{
			conn = dbMgr.getConnection();
			
			PreparedStatement stm = null;
			ResultSet result = null;
			try{
				stm = conn.prepareStatement("SELECT name FROM ontology_declared_properties WHERE id=?");
				stm.setInt(1, propertyId);
				result = stm.executeQuery();
				
				if (result.first() == false) throw new TagontoException("result set empty.");
				return result.getString("name");
				
			}finally{
				Exception exception = null;
				try {
					if (result != null) result.close();
				} catch (SQLException e) { exception = e;}
				
				try {
					if (stm != null) stm.close();
				} catch (SQLException e) { exception = e;}
				
				if (exception != null) throw new TagontoException("Unable to get property uri", exception);
			}
			
		} catch (SQLException e) {
			throw new TagontoException("Unable to get property uri :", e);
		} finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
}
