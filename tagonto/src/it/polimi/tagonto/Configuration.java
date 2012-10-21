package it.polimi.tagonto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import it.polimi.tagonto.mapper.User;

public class Configuration 
{
	private static Configuration instance = null; 
	
	private int ONTOLOGY_POOL_SIZE = 10;
	
	//*********************** STANDARD MAPPER CONFIGURATION *****************
	private float STANDARD_MAPPER_WEIGHT_LEVENSHTEIN = 1f;
	private float STANDARD_MAPPER_WEIGHT_WORDNET = 1f;
	private float STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD = 0.4f;
	private float STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER = 0.3f;
	
	//*********************** GREEDY MAPPER CONFIGURATION *******************
	private float GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD = 0.3f;
	private float GREEDY_MAPPER_NOISE_THRESHOLD = 0.4f;
	
	//*********************** WORDNET PLUGIN CONFIGURATION ******************
	private String WORDNET_JWNL_CONFIG_FILE = "config/file_properties.xml";
	private float WORDNET_FIRST_LEVEL_WEIGHT = 0.8f;
	private int WORDNET_HIERARCHY_MAX_DEPTH = 1;
	
	//*********************** GOOGLE CHOOSE PLUGIN CONFIGURATION ************
	private int GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES = 1;
	private int GOOGLE_CHOOSER_MAX_KEYWORDS = 10;
	
	//*********************** DATABASE CONFIGURATION ************************
	private String DATABASE_SERVER = "localhost";
	private String DATABASE_SCHEMA = "tagonto";
	private String DATABASE_USER = "tagonto";
	private String DATABASE_PASS = "tagonto";
	private int    DATABASE_CONNECTION_POOL_SIZE = 20;
	
	private int    DATABASE_TAGONTO_USER_ID = 1;
	private String DATABASE_TAGONTO_USERNAME = "#TAGONTO#";
	private User   DATABASE_TAGONTO_USER = new User(DATABASE_TAGONTO_USER_ID, DATABASE_TAGONTO_USERNAME);
	
	//*********************** RDF CACHER CONFIGURATION **********************
	private String RDF_CACHER_JDBC_URL = "jdbc:mysql://localhost/tagonto_model";
	private String RDF_CACHER_DB_USER = "jena";
	private String RDF_CACHER_DB_PASS = "jena";
	private String RDF_CACHER_DB_TYPE = "MySql";
	private String RDF_CACHER_DB_DRIVER = "com.mysql.jdbc.Driver";
	private String RDF_CACHER_TAG_ONTOLOGY_URI = "file:///var/develop/workspace/tagonto/ontologies/tagonto.owl";
	
	//*********************** RELATED TAG SERVICE ****************************
	private String RELATED_TAG_SERVICE_URL = "http://192.168.66.3/tagonto/TagontoNETREST.php?r=GetFriends&tag=";
	private int    RELATED_TAG_SERVICE_MAX_TAGS = 4;
	
	private Configuration()
	{
	}
	
	public static void createConfiguration(String configFile) throws TagontoException
	{
		if (Configuration.instance != null) throw new TagontoException("Configuration already created.");
		else{
			Configuration.instance = new Configuration();
			Properties properties = new Properties();
			
			if (configFile != null){
				InputStream configStream = null;
				try {
					configStream = new FileInputStream(configFile);
					properties.load(configStream);
				} catch (FileNotFoundException e) {
					throw new TagontoException("Configuration file not found : " + configFile);
				} catch (IOException e) {
					throw new TagontoException("Error while loading config file : " + configFile);
				}finally{
					try {
						if (configStream != null) configStream.close();
					} catch (IOException e) {
						throw new TagontoException("Error while loading config file : " + configFile);
					}
				}
			}
			
			// now load properties
			if (properties.getProperty("ONTOLOGY_POOL_SIZE") != null)
				Configuration.instance.ONTOLOGY_POOL_SIZE = Integer.parseInt(properties.getProperty("ONTOLOGY_POOL_SIZE"));
			
			if (properties.getProperty("STANDARD_MAPPER_WEIGHT_LEVENSHTEIN") != null)
				Configuration.instance.STANDARD_MAPPER_WEIGHT_LEVENSHTEIN = Float.parseFloat(properties.getProperty("STANDARD_MAPPER_WEIGHT_LEVENSHTEIN"));
			if (properties.getProperty("STANDARD_MAPPER_WEIGHT_WORDNET") != null)
				Configuration.instance.STANDARD_MAPPER_WEIGHT_WORDNET = Float.parseFloat(properties.getProperty("STANDARD_MAPPER_WEIGHT_WORDNET"));
			if (properties.getProperty("STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD") != null)
				Configuration.instance.STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD = Float.parseFloat(properties.getProperty("STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD"));
			if (properties.getProperty("STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER") != null)
				Configuration.instance.STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER = Float.parseFloat(properties.getProperty("STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER"));
				
			if (properties.getProperty("GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD") != null)
				Configuration.instance.GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD = Float.parseFloat(properties.getProperty("GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD"));
			if (properties.getProperty("GREEDY_MAPPER_NOISE_THRESHOLD") != null)
				Configuration.instance.GREEDY_MAPPER_NOISE_THRESHOLD = Float.parseFloat(properties.getProperty("GREEDY_MAPPER_NOISE_THRESHOLD"));
				
			if (properties.getProperty("WORDNET_JWNL_CONFIG_FILE") != null)
				Configuration.instance.WORDNET_JWNL_CONFIG_FILE = properties.getProperty("WORDNET_JWNL_CONFIG_FILE");
			if (properties.getProperty("WORDNET_FIRST_LEVEL_WEIGHT") != null)
				Configuration.instance.WORDNET_FIRST_LEVEL_WEIGHT = Float.parseFloat(properties.getProperty("WORDNET_FIRST_LEVEL_WEIGHT"));
			if (properties.getProperty("WORDNET_HIERARCHY_MAX_DEPTH") != null)
				Configuration.instance.WORDNET_HIERARCHY_MAX_DEPTH = Integer.parseInt(properties.getProperty("WORDNET_HIERARCHY_MAX_DEPTH"));
				
			if (properties.getProperty("GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES") != null)
				Configuration.instance.GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES = Integer.parseInt(properties.getProperty("GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES"));
			if (properties.getProperty("GOOGLE_CHOOSER_MAX_KEYWORDS") != null)
				Configuration.instance.GOOGLE_CHOOSER_MAX_KEYWORDS = Integer.parseInt(properties.getProperty("GOOGLE_CHOOSER_MAX_KEYWORDS"));
				
			if (properties.getProperty("DATABASE_SERVER") != null)
				Configuration.instance.DATABASE_SERVER = properties.getProperty("DATABASE_SERVER");
			if (properties.getProperty("DATABASE_SCHEMA") != null)
				Configuration.instance.DATABASE_SCHEMA = properties.getProperty("DATABASE_SCHEMA");
			if (properties.getProperty("DATABASE_USER") != null)
				Configuration.instance.DATABASE_USER = properties.getProperty("DATABASE_USER");
			if (properties.getProperty("DATABASE_PASS") != null)
				Configuration.instance.DATABASE_PASS = properties.getProperty("DATABASE_PASS");
			if (properties.getProperty("DATABASE_CONNECTION_POOL_SIZE") != null)
				Configuration.instance.DATABASE_CONNECTION_POOL_SIZE = Integer.parseInt(properties.getProperty("DATABASE_CONNECTION_POOL_SIZE"));

			if (properties.getProperty("DATABASE_TAGONTO_USER_ID") != null)
				Configuration.instance.DATABASE_TAGONTO_USER_ID = Integer.parseInt(properties.getProperty("DATABASE_TAGONTO_USER_ID"));
			if (properties.getProperty("DATABASE_TAGONTO_USERNAME") != null)
				Configuration.instance.DATABASE_TAGONTO_USERNAME = properties.getProperty("DATABASE_TAGONTO_USERNAME");
			
			Configuration.instance.DATABASE_TAGONTO_USER = new User(Configuration.instance.DATABASE_TAGONTO_USER_ID, Configuration.instance.DATABASE_TAGONTO_USERNAME);
				
			if (properties.getProperty("RDF_CACHER_JDBC_URL") != null)
				Configuration.instance.RDF_CACHER_JDBC_URL = properties.getProperty("RDF_CACHER_JDBC_URL");
			if (properties.getProperty("RDF_CACHER_DB_USER") != null)
				Configuration.instance.RDF_CACHER_DB_USER = properties.getProperty("RDF_CACHER_DB_USER");
			if (properties.getProperty("RDF_CACHER_DB_PASS") != null)
				Configuration.instance.RDF_CACHER_DB_PASS = properties.getProperty("RDF_CACHER_DB_PASS");
			if (properties.getProperty("RDF_CACHER_DB_TYPE") != null)
				Configuration.instance.RDF_CACHER_DB_TYPE = properties.getProperty("RDF_CACHER_DB_TYPE");
			if (properties.getProperty("RDF_CACHER_DB_DRIVER") != null)
				Configuration.instance.RDF_CACHER_DB_DRIVER = properties.getProperty("RDF_CACHER_DB_DRIVER");
			if (properties.getProperty("RDF_CACHER_TAG_ONTOLOGY_URI") != null)
				Configuration.instance.RDF_CACHER_TAG_ONTOLOGY_URI = properties.getProperty("RDF_CACHER_TAG_ONTOLOGY_URI");
			
			if (properties.getProperty("RELATED_TAG_SERVICE_URL") != null)
				Configuration.instance.RELATED_TAG_SERVICE_URL = properties.getProperty("RELATED_TAG_SERVICE_URL");
			if (properties.getProperty("RELATED_TAG_SERVICE_MAX_TAGS") != null)
				Configuration.instance.RELATED_TAG_SERVICE_MAX_TAGS = Integer.parseInt(properties.getProperty("RELATED_TAG_SERVICE_MAX_TAGS"));
		}
	}
	
	static void debug_createConfig(String configFile) throws TagontoException
	{
		Configuration.instance = new Configuration();
		Properties properties = new Properties();
		
		properties.setProperty("ONTOLOGY_POOL_SIZE", Integer.toString(Configuration.instance.ONTOLOGY_POOL_SIZE));
		
		properties.setProperty("STANDARD_MAPPER_WEIGHT_LEVENSHTEIN", Float.toString(Configuration.instance.STANDARD_MAPPER_WEIGHT_LEVENSHTEIN));
		properties.setProperty("STANDARD_MAPPER_WEIGHT_WORDNET", Float.toString(Configuration.instance.STANDARD_MAPPER_WEIGHT_WORDNET));
		properties.setProperty("STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD", Float.toString(Configuration.instance.STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD));
		properties.setProperty("STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER", Float.toString(Configuration.instance.STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER));
		
		properties.setProperty("GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD", Float.toString(Configuration.instance.GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD));
		properties.setProperty("GREEDY_MAPPER_NOISE_THRESHOLD", Float.toString(Configuration.instance.GREEDY_MAPPER_NOISE_THRESHOLD));
			
		properties.setProperty("WORDNET_JWNL_CONFIG_FILE", Configuration.instance.WORDNET_JWNL_CONFIG_FILE);
		properties.setProperty("WORDNET_FIRST_LEVEL_WEIGHT", Float.toString(Configuration.instance.WORDNET_FIRST_LEVEL_WEIGHT));
		properties.setProperty("WORDNET_HIERARCHY_MAX_DEPTH", Integer.toString(Configuration.instance.WORDNET_HIERARCHY_MAX_DEPTH));
			
		properties.setProperty("GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES", Integer.toString(Configuration.instance.GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES));
		properties.setProperty("GOOGLE_CHOOSER_MAX_KEYWORDS", Integer.toString(Configuration.instance.GOOGLE_CHOOSER_MAX_KEYWORDS));
		
		properties.setProperty("DATABASE_SERVER", Configuration.instance.DATABASE_SERVER);
		properties.setProperty("DATABASE_SCHEMA", Configuration.instance.DATABASE_SCHEMA);
		properties.setProperty("DATABASE_USER", Configuration.instance.DATABASE_USER);
		properties.setProperty("DATABASE_PASS", Configuration.instance.DATABASE_PASS);
		properties.setProperty("DATABASE_CONNECTION_POOL_SIZE", Integer.toString(Configuration.instance.DATABASE_CONNECTION_POOL_SIZE));

		properties.setProperty("DATABASE_TAGONTO_USER_ID", Integer.toString(Configuration.instance.DATABASE_TAGONTO_USER_ID));
		properties.setProperty("DATABASE_TAGONTO_USERNAME", Configuration.instance.DATABASE_TAGONTO_USERNAME);
			
		properties.setProperty("RDF_CACHER_JDBC_URL", Configuration.instance.RDF_CACHER_JDBC_URL);
		properties.setProperty("RDF_CACHER_DB_USER", Configuration.instance.RDF_CACHER_DB_USER);
		properties.setProperty("RDF_CACHER_DB_PASS", Configuration.instance.RDF_CACHER_DB_PASS);
		properties.setProperty("RDF_CACHER_DB_TYPE", Configuration.instance.RDF_CACHER_DB_TYPE);
		properties.setProperty("RDF_CACHER_DB_DRIVER", Configuration.instance.RDF_CACHER_DB_DRIVER);
		properties.setProperty("RDF_CACHER_TAG_ONTOLOGY_URI", Configuration.instance.RDF_CACHER_TAG_ONTOLOGY_URI);
		
		properties.setProperty("RELATED_TAG_SERVICE_URL", Configuration.instance.RELATED_TAG_SERVICE_URL);
		properties.setProperty("RELATED_TAG_SERVICE_MAX_TAGS", Integer.toString(Configuration.instance.RELATED_TAG_SERVICE_MAX_TAGS));
		
		OutputStream configStream = null;
		try {
			configStream = new FileOutputStream(configFile);
			properties.store(configStream, null);
		} catch (FileNotFoundException e) {
			throw new TagontoException("Configuration file not found : " + configFile);
		} catch (IOException e) {
			throw new TagontoException("Error while storing config file : " + configFile);
		}finally{
			try {
				if (configStream != null) configStream.close();
			} catch (IOException e) {
				throw new TagontoException("Error while storing config file : " + configFile);
			}
		}
	}
	
	/**
	 * Returns the configuration object if already created, otherwise null.
	 * @return the configuration object.
	 */
	public static Configuration getInstance()
	{
		return Configuration.instance; 
	}
	
	public int getONTOLOGY_POOL_SIZE()
	{
		return this.ONTOLOGY_POOL_SIZE;
	}

	public float getSTANDARD_MAPPER_WEIGHT_LEVENSHTEIN() {
		return STANDARD_MAPPER_WEIGHT_LEVENSHTEIN;
	}
	
	public float getSTANDARD_MAPPER_WEIGHT_WORDNET() {
		return STANDARD_MAPPER_WEIGHT_WORDNET;
	}

	public float getSTANDARD_MAPPER_SIGNIFICANCE_THRESHOLD() {
		return STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD;
	}

	public float getSTANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER() {
		return STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER;
	}

	public String getWORDNET_JWNL_CONFIG_FILE() {
		return WORDNET_JWNL_CONFIG_FILE;
	}

	public float getWORDNET_FIRST_LEVEL_WEIGHT() {
		return WORDNET_FIRST_LEVEL_WEIGHT;
	}

	public int getWORDNET_HIERARCHY_MAX_DEPTH() {
		return WORDNET_HIERARCHY_MAX_DEPTH;
	}

	public float getGREEDY_MAPPER_SIGNIFICANCE_THRESHOLD() {
		return GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD;
	}

	public float getGREEDY_MAPPER_NOISE_THRESHOLD() {
		return GREEDY_MAPPER_NOISE_THRESHOLD;
	}

	public int getDATABASE_CONNECTION_POOL_SIZE() {
		return DATABASE_CONNECTION_POOL_SIZE;
	}

	public String getDATABASE_SCHEMA() {
		return DATABASE_SCHEMA;
	}

	public String getDATABASE_SERVER() {
		return DATABASE_SERVER;
	}

	public String getDATABASE_PASS() {
		return DATABASE_PASS;
	}

	public String getDATABASE_USER() {
		return DATABASE_USER;
	}

	public User getDATABASE_TAGONTO_USER() {
		return DATABASE_TAGONTO_USER;
	}

	public int getGOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES() {
		return GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES;
	}

	public int getGOOGLE_CHOOSER_MAX_KEYWORDS() {
		return GOOGLE_CHOOSER_MAX_KEYWORDS;
	}

	public String getRDF_CACHER_DB_DRIVER() {
		return RDF_CACHER_DB_DRIVER;
	}

	public String getRDF_CACHER_DB_PASS() {
		return RDF_CACHER_DB_PASS;
	}

	public String getRDF_CACHER_DB_TYPE() {
		return RDF_CACHER_DB_TYPE;
	}

	public String getRDF_CACHER_DB_USER() {
		return RDF_CACHER_DB_USER;
	}

	public String getRDF_CACHER_JDBC_URL() {
		return RDF_CACHER_JDBC_URL;
	}

	public String getRDF_CACHER_TAG_ONTOLOGY_URI() {
		return RDF_CACHER_TAG_ONTOLOGY_URI;
	}

	public String getRELATED_TAG_SERVICE_URL() {
		return RELATED_TAG_SERVICE_URL;
	}

	public int getRELATED_TAG_SERVICE_MAX_TAGS() {
		return RELATED_TAG_SERVICE_MAX_TAGS;
	}
	
	
}
