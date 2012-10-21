package it.polimi.tagonto.mapper.caching;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.IMapper;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.Ontology;
import it.polimi.tagonto.mapper.Tag;
import it.polimi.tagonto.mapper.User;
import it.polimi.tagonto.mapper.utility.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;

public class DbMappingCacher implements IMappingCacher 
{
	private static DbMappingCacher instance = new DbMappingCacher();
	
	private DbConnectionManager dbMgr = DbConnectionManager.getInstance();
	
	private DbMappingCacher()
	{
	}
	
	public static DbMappingCacher getInstance()
	{
		return DbMappingCacher.instance;
	}
	
	public void cacheMappings(Collection<Mapping> mappings)
		throws TagontoException
	{
		for(Mapping mapping : mappings){
			this.cacheMapping(mapping);
		}
	}
	
	public void cacheMapping(Mapping mapping) throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		
		try{
			conn = this.dbMgr.getConnection();
			
			int conceptId = -1;
			try{
				stm = conn.prepareStatement("SELECT id FROM ontology_concepts WHERE name=? AND ontology=?");
				stm.setString(1, mapping.getConcept().getURI());
				stm.setInt(2, mapping.getOntology().getId());

				result = stm.executeQuery();
				result.first();
				conceptId = result.getInt("id");
			}catch(SQLException e){
				throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
			}finally{
				Exception exception = null;
				try {
					if (result != null) result.close();
				} catch (SQLException e) { exception = e;}
				
				try {
					if (stm != null) stm.close();
				} catch (SQLException e) { exception = e;}
				
				result = null;
				stm = null;
				
				if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
			}
			
			try{
				stm = conn.prepareStatement("INSERT INTO mappings (tag, concept, user, significance) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE significance = ?");
				
				stm.setString(1, mapping.getTag().getTag());
				stm.setInt(2, conceptId);
				stm.setInt(3, mapping.getUser().getId());
				stm.setFloat(4, mapping.getSignificance());
				stm.setFloat(5, mapping.getSignificance());
				
				stm.execute();
			}catch(SQLException e){
				throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
			}finally{
				Exception exception = null;
				try {
					if (stm != null) stm.close();
					stm = null;
				} catch (SQLException e) { exception = e;}
				
				stm = null;
				
				if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
			}
			
			this.cacheMappers(conn, mapping, conceptId);
		}finally{
			Exception exception = null;
			
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	public Collection<Mapping> getCachedMappings(Tag tag, Ontology ontology, User user) throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		
		try{
			conn = this.dbMgr.getConnection();
			
			try{
				String query = "SELECT m.id,c.name,m.significance FROM mappings AS m JOIN ontology_concepts AS c " +
						"WHERE c.id=m.concept AND m.tag = ? AND c.ontology = ? AND m.user = ?";
				
				stm = conn.prepareStatement(query);
				stm.setString(1, tag.getTag());
				stm.setInt(2, ontology.getId());
				stm.setInt(3, user.getId());

				result = stm.executeQuery();
				
				Collection<Mapping> mappings = new Vector<Mapping>();
				if (result.first() == false) return mappings;
				
				do{
					String conceptURI = result.getString("name");
					float significance = result.getFloat("significance");
					int mappingId = result.getInt("id");
					
					OntClass concept = ontology.getConcept(conceptURI);
					
					if (concept != null){
						Mapping mapping = new Mapping(tag, concept, ontology, user, significance);
						this.retrieveMappersById(mapping, mappingId);
						mappings.add(mapping);
					}
				}while(result.next() == true);
				
				return mappings;
			}catch(SQLException e){
				throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
			}finally{
				Exception exception = null;
				try {
					if (result != null) result.close();
				} catch (SQLException e) { exception = e;}
				
				try {
					if (stm != null) stm.close();
				} catch (SQLException e) { exception = e;}
				
				result = null;
				stm = null;
				
				if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
			}
		}finally{
			Exception exception = null;
			
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	public Collection<Mapping> getAggregatedCachedMappings(Tag tag, Ontology ontology)  
		throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		
		try{
			conn = this.dbMgr.getConnection();
			
			stm = conn.prepareStatement("SELECT c.name, m.significance, c.id AS conceptId FROM concrete_mappings AS m JOIN ontology_concepts AS c WHERE " +
											"m.tag = ? AND c.ontology = ? AND c.id=m.concept");
			
			stm.setString(1, tag.getTag());
			stm.setInt(2, ontology.getId());
			result = stm.executeQuery();
			
			Collection<Mapping> mappings = new Vector<Mapping>();
			if (result.first() == false) return mappings;
			
			do{
				String conceptURI = result.getString("name");
				float significance = result.getFloat("significance");
				int conceptId = result.getInt("conceptId");
				
				OntClass concept = ontology.getConcept(conceptURI);
				
				if (concept != null){
					User tagontoUser = Configuration.getInstance().getDATABASE_TAGONTO_USER();
					Mapping mapping = new Mapping(tag, concept, ontology, tagontoUser, significance);
					this.retrieveMappers(mapping, conceptId);
					mappings.add(mapping);
				}
			}while(result.next() == true);
			
			return mappings;
			
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
			
			result = null;
			stm = null;
			
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void retrieveMappersById(Mapping mapping, int mappingId) throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		
		try{
			conn = this.dbMgr.getConnection();
			
			stm = conn.prepareStatement("SELECT mapper FROM mapped_by WHERE mapping_id=?");
			stm.setInt(1, mappingId);
			
			result = stm.executeQuery();
			
			if (result.first() == false) return;
			else{
				do{
					String mapper = result.getString("mapper");
					try {
						Class<? extends IMapper> mapperClass = (Class<? extends IMapper>) Class.forName(mapper);
						mapping.addMapper(mapperClass);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						// log but do not stop, just do not include the mapper in the list 
					}
					
				}while(result.next() == true);
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
			
			result = null;
			stm = null;
			
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void retrieveMappers(Mapping mapping, int conceptId) throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		
		try{
			conn = this.dbMgr.getConnection();
			
			stm = conn.prepareStatement("SELECT mapper FROM mapped_by WHERE " +
										"mapping_id IN " +
											"(SELECT id FROM mappings WHERE tag=? AND concept=?)");
			
			stm.setString(1, mapping.getTag().getTag());
			stm.setInt(2, conceptId);
			
			result = stm.executeQuery();
			
			if (result.first() == false) return;
			else{
				do{
					String mapper = result.getString("mapper");
					try {
						Class<? extends IMapper> mapperClass = (Class<? extends IMapper>) Class.forName(mapper);
						mapping.addMapper(mapperClass);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						// log but do not stop, just do not include the mapper in the list 
					}
					
				}while(result.next() == true);
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
			
			result = null;
			stm = null;
			
			try {
				if (conn != null) conn.commit();
			} catch (SQLException e) { exception = e;}
			
			this.dbMgr.releaseConnection(conn);
			if (exception != null) throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, exception.getMessage()), exception);
		}
	}
	
	private void cacheMappers(Connection conn, Mapping mapping, int conceptId) throws TagontoException
	{
		PreparedStatement stm = null;
		
		for(Class<? extends IMapper> mapper : mapping.getMappers())
		{
			try{
				stm = conn.prepareStatement("INSERT IGNORE INTO mapped_by (mapping_id, mapper) " +
												"SELECT id,? FROM mappings WHERE tag=? AND concept=? AND user=?");
				stm.setString(1, mapper.getCanonicalName());
				stm.setString(2, mapping.getTag().getTag());
				stm.setInt(3, conceptId);
				stm.setInt(4, mapping.getUser().getId());
				
				stm.execute();
			}catch(SQLException e){
				throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
			}finally{
				try {
					if (stm != null) stm.close();
				} catch (SQLException e) {
					throw new TagontoException(String.format(Resources.MSG_CACHER_CACHING_FAILED, e.getMessage()), e);
				}
			}
		}
	}
}

	