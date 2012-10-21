package it.polimi.tagonto.mapper.caching;

import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.utility.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFUntypedResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFProperty;

public class OntologyCacher implements IResourceCacher 
{
	private static OntologyCacher instance = new OntologyCacher();
	private DbConnectionManager dbMgr = DbConnectionManager.getInstance();
	
	private static class PropertyEntry
	{
		private String subjectUri = null;
		private String propertyUri = null;
		private String objectUri = null;
		
		private PropertyEntry(String subjectUri, String propertyUri, String objectUri)
		{
			this.subjectUri = subjectUri;
			this.propertyUri = propertyUri;
			this.objectUri = objectUri;
		}

		public String getObjectUri() {
			return objectUri;
		}

		public String getPropertyUri() {
			return propertyUri;
		}

		public String getSubjectUri() {
			return subjectUri;
		}
	}
	
	private static class HierarchyEntry
	{
		private String subClassUri = null;
		private String superClassUri = null;
		
		private HierarchyEntry(String subClassUri, String superClassUri)
		{
			this.subClassUri = subClassUri;
			this.superClassUri = superClassUri;
		}

		public String getSubClassUri() {
			return subClassUri;
		}

		public String getSuperClassUri() {
			return superClassUri;
		}	
	}
	
	private static class InstanceEntry
	{
		private String conceptUri = null;
		private String instanceUri = null;
		
		private InstanceEntry(String conceptUri, String instanceUri)
		{
			this.conceptUri = conceptUri;
			this.instanceUri = instanceUri;
		}

		public String getConceptUri() {
			return conceptUri;
		}

		public String getInstanceUri() {
			return instanceUri;
		}
	}
	
	private static class ReachableInstanceEntry
	{
		private String subjectUri = null;
		private String propertyUri = null;
		private String instanceUri = null;
		
		private ReachableInstanceEntry(String subjectUri, String propertyUri, String instanceUri)
		{
			this.subjectUri = subjectUri;
			this.propertyUri = propertyUri;
			this.instanceUri = instanceUri;
		}

		public String getInstanceUri() {
			return instanceUri;
		}

		public String getPropertyUri() {
			return propertyUri;
		}

		public String getSubjectUri() {
			return subjectUri;
		}
	}
	
	private OntologyCacher()
	{
	}
	
	public static OntologyCacher getInstance()
	{
		return OntologyCacher.instance;
	}
	
	public void cacheOntology(Ontology ontology) throws TagontoException
	{
		Connection conn = null;
		try {
			conn = this.dbMgr.getConnection();
			
			PreparedStatement stm = null;
			try{
				stm = conn.prepareStatement("INSERT INTO ontologies (url) VALUES (?)");
				stm.setString(1, ontology.getUri());
				stm.execute();
			}finally{
				if (stm != null){
					stm.close();
					stm = null;
				}
			}
			
			ResultSet result = null;
			try{
				stm = conn.prepareStatement("SELECT id FROM ontologies WHERE  url=?");
				stm.setString(1, ontology.getUri());
				result = stm.executeQuery();
			
				result.first();
				ontology.setId(result.getInt("id"));
			}finally{
				Exception exception = null;
				try {
					if (result != null) result.close();
				} catch (SQLException e) { exception = e;}
				
				try {
					if (stm != null) stm.close();
				} catch (SQLException e) { exception = e;}
				
				if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
			}
			
			for(OntClass concept : ontology.getNamedConcepts()){
				try{
					stm = conn.prepareStatement("INSERT INTO ontology_concepts (name,ontology) VALUES (?,?)");
					stm.setString(1, concept.getURI());
					stm.setInt(2, ontology.getId());
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}	
			}
			
			for(Ontology.PropertyStat stat : ontology.getPropertyStats().values()){
				int conceptId1 = 0;
				int conceptId2 = 0;
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name = ? AND ontology = ?");
					stm.setString(1, stat.getFirstConcept().getURI());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
					if (result.first() == false) throw new TagontoException();
					conceptId1 = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name = ? AND ontology = ?");
					stm.setString(1, stat.getSecondConcept().getURI());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
					if (result.first() == false) throw new TagontoException();
					conceptId2 = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("INSERT INTO ontology_properties (firstConcept,secondConcept,ontology,count) VALUES (?,?,?,?)");
					stm.setInt(1, conceptId1);
					stm.setInt(2, conceptId2);
					stm.setInt(3, ontology.getId());
					stm.setInt(4, stat.getCount());
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}
			}
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
		
		this.cacheOntologyProperties(ontology);
		this.cachePropertyEntries(ontology);
		this.cacheHierarchy(ontology);
		this.cacheInstances(ontology);
		this.cacheReachableInstances(ontology);
	}
	
	private void cacheOntologyProperties(Ontology ontology) throws TagontoException
	{
		Set<String> namedProperties = this.generatePropertiesToCache(ontology);
		
		Connection conn = null;
		try {
			conn = this.dbMgr.getConnection();
			
			PreparedStatement stm = null;
			
			for(String propertyUri : namedProperties){
				try{
					stm = conn.prepareStatement("INSERT IGNORE INTO ontology_declared_properties (name,ontology) VALUES (?,?)");
					stm.setString(1, propertyUri);
					stm.setInt(2, ontology.getId());
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}
			}
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void cachePropertyEntries(Ontology ontology) throws TagontoException
	{
		Collection<PropertyEntry> entries = this.generatePropertyEntries(ontology);
		
		Connection conn = null;
		try {
			conn = this.dbMgr.getConnection();
			
			PreparedStatement stm = null;
			ResultSet result = null;
			
			for(PropertyEntry entry : entries){
				
				Integer subjectId = null;
				Integer objectId = null;
				Integer propertyId = null;
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
					stm.setString(1, entry.getSubjectUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					subjectId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
					stm.setString(1, entry.getObjectUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					objectId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_declared_properties WHERE name=? AND ontology=?");
					stm.setString(1, entry.getPropertyUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					propertyId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("INSERT IGNORE INTO domain_range (subject,property,object) VALUES (?,?,?)");
					stm.setInt(1, subjectId);
					stm.setInt(2, propertyId);
					stm.setInt(3, objectId);
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}
			}
			
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void cacheHierarchy(Ontology ontology) throws TagontoException
	{
		Collection<HierarchyEntry> entries = this.generateHierarchyEntries(ontology);
		
		Connection conn = null;
		try {
			conn = this.dbMgr.getConnection();
			
			PreparedStatement stm = null;
			ResultSet result = null;
			
			for(HierarchyEntry entry : entries){
				
				Integer subClassId = null;
				Integer superClassId = null;
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
					stm.setString(1, entry.getSubClassUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					subClassId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
					stm.setString(1, entry.getSuperClassUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					superClassId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("INSERT IGNORE INTO subclassof (subclass,superclass) VALUES (?,?)");
					stm.setInt(1, subClassId);
					stm.setInt(2, superClassId);
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}
			}
			
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void cacheInstances(Ontology ontology) throws TagontoException
	{
		Collection<InstanceEntry> entries = this.generateInstanceEntries(ontology);
		
		Connection conn = null;
		try {
			conn = this.dbMgr.getConnection();
			
			PreparedStatement stm = null;
			ResultSet result = null;
			
			for(InstanceEntry entry : entries){
				
				Integer conceptId = null;
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
					stm.setString(1, entry.getConceptUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					conceptId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("INSERT IGNORE INTO instances (concept,instanceUri) VALUES (?,?)");
					stm.setInt(1, conceptId);
					stm.setString(2, entry.getInstanceUri());
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}
			}
			
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void cacheReachableInstances(Ontology ontology) throws TagontoException
	{
		Collection<ReachableInstanceEntry> entries = this.generateReachableInstanceEntries(ontology);
		
		Connection conn = null;
		try {
			conn = this.dbMgr.getConnection();
			
			PreparedStatement stm = null;
			ResultSet result = null;
			
			for(ReachableInstanceEntry entry : entries){
				
				Integer subjectId = null;
				Integer propertyId = null;
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
					stm.setString(1, entry.getSubjectUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					subjectId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("SELECT id FROM ontology_declared_properties WHERE name=? AND ontology=?");
					stm.setString(1, entry.getPropertyUri());
					stm.setInt(2, ontology.getId());
					result = stm.executeQuery();
				
					if (result.first() == false) 
						continue;
					propertyId = result.getInt("id");
				}finally{
					Exception exception = null;
					try {
						if (result != null) result.close();
					} catch (SQLException e) { exception = e;}
					
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) { exception = e;}
					
					if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
				}
				
				try{
					stm = conn.prepareStatement("INSERT IGNORE INTO reachable_instances (subject,property,instanceUri) VALUES (?,?,?)");
					stm.setInt(1, subjectId);
					stm.setInt(2, propertyId);
					stm.setString(3, entry.getInstanceUri());
					stm.execute();
				}finally{
					try {
						if (stm != null) stm.close();
					} catch (SQLException e) {throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);}
				}
			}
			
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private Set<String> generatePropertiesToCache(Ontology ontology)
	{	
		Set<String> namedProperties = new HashSet<String>();
		
		ExtendedIterator it = ontology.getModel().listObjectProperties();
		while(it.hasNext()){
			OntProperty prop = (OntProperty) it.next();
			namedProperties.add(prop.getURI());
		}
		
		return namedProperties;
	}
	
	private Collection<PropertyEntry> generatePropertyEntries(Ontology ontology) throws TagontoException
	{
		Collection<PropertyEntry> results = new Vector<PropertyEntry>();
		Map<String,String> prefixMap = ontology.getModel().getNsPrefixMap();
		
		OWLModel protegeModel = null;
		try { 
			String uriPrefix = prefixMap.get("");
			protegeModel = ProtegeOWL.createJenaOWLModelFromURI(ontology.getUri());
			protegeModel.getNamespaceManager().setDefaultNamespace(uriPrefix);
		} catch (Exception e) {
			throw new TagontoException("Unable to generate property entries : " , e);
		}
		
		Set<String> propertyUris = this.generatePropertiesToCache(ontology);
		Collection<OntClass> concepts = ontology.getNamedConcepts();
		try{
			for(OntClass concept : concepts){
				OWLNamedClass protegeConcept = protegeModel.getOWLNamedClass(concept.getLocalName());
				if (protegeConcept == null){ // maybe its an external resource
					String prefix = null;
					String nameSpace = protegeModel.getNamespaceForURI(concept.getURI());
					for(String currentPrefix : prefixMap.keySet()){
						String value = prefixMap.get(currentPrefix);
						if (value.equals(nameSpace)){
							prefix = currentPrefix;
							break;
						}
					}
					String conceptUri = prefix + ":" + concept.getLocalName();
					protegeConcept = protegeModel.getOWLNamedClass(conceptUri);
				}
				
				if (protegeConcept == null) continue;
				
				Set<DefaultRDFProperty> associatedProperties = protegeConcept.getAssociatedProperties();
				for(DefaultRDFProperty property : associatedProperties){
					String propertyUri = property.getURI();
					if (propertyUris.contains(propertyUri)){ // this is a named property
						String domainUri = protegeConcept.getURI();
						Collection<RDFResource> ranges = property.getRanges(false);
						for(RDFResource range : ranges){
							String rangeUri = range.getURI();
							PropertyEntry entry = new PropertyEntry(domainUri, propertyUri, rangeUri);
							results.add(entry);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new TagontoException(e);
		}
		
		return results;
	}
	
	private Collection<HierarchyEntry> generateHierarchyEntries(Ontology ontology) throws TagontoException
	{
		Collection<HierarchyEntry> results = new Vector<HierarchyEntry>();
		
		Collection<OntClass> concepts = ontology.getNamedConcepts();
		for(OntClass concept : concepts){
			ExtendedIterator it = concept.listSuperClasses();
			while(it.hasNext()){
				OntClass superClass = (OntClass)it.next();
				results.add(new HierarchyEntry(concept.getURI(), superClass.getURI()));
			}
		}
		
		return results;
	}
	
	private Collection<InstanceEntry> generateInstanceEntries(Ontology ontology) throws TagontoException
	{
		Collection<InstanceEntry> results = new Vector<InstanceEntry>();
		
		Collection<OntClass> concepts = ontology.getNamedConcepts();
		for(OntClass concept : concepts){
			ExtendedIterator it = concept.listInstances();
			while(it.hasNext()){
				Individual instance = (Individual)it.next();
				results.add(new InstanceEntry(concept.getURI(), instance.getURI()));
			}
		}
		
		return results;
	}
	
	private Collection<ReachableInstanceEntry> generateReachableInstanceEntries(Ontology ontology) throws TagontoException
	{
		Collection<ReachableInstanceEntry> results = new Vector<ReachableInstanceEntry>();
		
		Collection<OntClass> concepts = ontology.getNamedConcepts();
		for(OntClass subject : concepts){
			// approximate, just use the first instance
			ExtendedIterator subjectInstancesIt = subject.listInstances();
			if (!subjectInstancesIt.hasNext()) continue;
			Individual subjectInstance = (Individual)subjectInstancesIt.next();
			
			ExtendedIterator propertyIterator = ontology.getModel().listObjectProperties();
			while(propertyIterator.hasNext()){
				ObjectProperty property = (ObjectProperty)propertyIterator.next();
				NodeIterator instancesIt = ontology.getModel().listObjectsOfProperty(subjectInstance, property);
				while(instancesIt.hasNext()){
					RDFNode rdfNode = instancesIt.nextNode();
					if (rdfNode.isURIResource() == true){
						String instanceUri = rdfNode.asNode().getURI();
						results.add(new ReachableInstanceEntry(subject.getURI(), property.getURI(), instanceUri));
					}
				}
			}
		}
		
		return results;
	}
	
	
	public boolean loadOntology(Ontology ontology) throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		try {
			conn = this.dbMgr.getConnection();
			
			stm = conn.prepareStatement("SELECT id, last_update FROM ontologies WHERE url=?");
			stm.setString(1, ontology.getUri());
			
			result = stm.executeQuery();
			if (result.first() == false) return false;
			else{
				ontology.setId(result.getInt("id"));
				return true;
			}
		} catch (SQLException e) {
			throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
		}finally{
			Exception exception = null;
			try {
				if (result != null) result.close();
			} catch (SQLException e) { exception = e;}
			
			try {
				if (stm != null) stm.close();
			} catch (SQLException e) { exception = e;}
			
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
}
