package it.polimi.tagonto.mapper.utility;

import it.polimi.tagonto.Configuration;
import it.polimi.tagonto.TagontoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The Db Connection Manager manages connections to the db.
 * 
 * @author Mauro Luigi Drago
 *
 */
public class DbConnectionManager 
{
	private static DbConnectionManager instance = new DbConnectionManager();
	private static final long WAIT_TIME = 300; 
	
	private boolean isInited = false;
	private int poolSize = Configuration.getInstance().getDATABASE_CONNECTION_POOL_SIZE();
	
	private Collection<Connection> freeConnections = new Vector<Connection>();
	private Collection<Connection> usedConnections = new Vector<Connection>();
	private ReentrantReadWriteLock connectionsLock = new ReentrantReadWriteLock();
	
	public static DbConnectionManager getInstance()
	{
		return DbConnectionManager.instance;
	}
	
	private void init() throws TagontoException
	{
		if (this.isInited == true) return;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			throw new TagontoException(e);
		}
		
		Configuration conf = Configuration.getInstance();
		String jdbcEndPoint = "jdbc:mysql://" + conf.getDATABASE_SERVER() + "/" + conf.getDATABASE_SCHEMA() +
							  "?" + "user=" + conf.getDATABASE_USER() + "&" + "password=" + conf.getDATABASE_PASS();
		
		// init the connection pool
		for(int i = 0; i < this.poolSize; i++){
			try {
				Connection conn = DriverManager.getConnection(jdbcEndPoint);
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.freeConnections.add(conn);
			} catch (SQLException e) {
				throw new TagontoException(e);
			}		
		}
		
		this.isInited = true;
	}
	
	public Connection getConnection() throws TagontoException
	{
		if (this.isInited == false) this.init();
		
		while(true){
			this.connectionsLock.readLock().lock();
			try{
				while(this.freeConnections.size() == 0){
					try {
						Thread.sleep(DbConnectionManager.WAIT_TIME);
					} catch (InterruptedException e) {
						throw new TagontoException(e);
					}
				}
			}finally{
				this.connectionsLock.readLock().unlock();
			}
			
			this.connectionsLock.writeLock().lock();
			try{
				if (this.freeConnections.size() != 0){
					Connection conn = this.freeConnections.iterator().next();
					this.freeConnections.remove(conn);
					this.usedConnections.add(conn);
					return conn;
				}
			}finally{
				this.connectionsLock.writeLock().unlock();
			}
		}
	}
	
	public void releaseConnection(Connection conn)
	{
		if (this.isInited == false) return;
		
		while(true){
			this.connectionsLock.readLock().lock();
			try{
				if (this.usedConnections.size() == 0) return;
				if (!(this.usedConnections.contains(conn))) return;
			}finally{
				this.connectionsLock.readLock().unlock();
			}
			
			this.connectionsLock.writeLock().lock();
			try{
				if (this.usedConnections.size() == 0) return;
				if (!(this.usedConnections.contains(conn))) return;
				
				this.usedConnections.remove(conn);
				this.freeConnections.add(conn);
			}finally{
				this.connectionsLock.writeLock().unlock();
			}
		}
	}
}
