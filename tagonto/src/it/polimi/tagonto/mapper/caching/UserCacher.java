package it.polimi.tagonto.mapper.caching;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.Resources;
import it.polimi.tagonto.TagontoException;
import it.polimi.tagonto.mapper.Mapping;
import it.polimi.tagonto.mapper.User;
import it.polimi.tagonto.mapper.utility.DbConnectionManager;

public class UserCacher implements IResourceCacher 
{
	private static UserCacher instance = new UserCacher();
	
	private DbConnectionManager dbMgr = DbConnectionManager.getInstance();
	
	private UserCacher()
	{	
	}
	
	public static UserCacher getInstance()
	{
		return UserCacher.instance;
	}
	
	public User getCachedUser(int userId) throws TagontoException
	{
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet result = null;
		
		try{
			conn = this.dbMgr.getConnection();
			
			stm = conn.prepareStatement("SELECT username FROM users WHERE id = ?");
			stm.setInt(1, userId);
			
			result = stm.executeQuery();
			if (result.first() == false) return null;
			else{
				return new User(userId, result.getString("username"));
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
