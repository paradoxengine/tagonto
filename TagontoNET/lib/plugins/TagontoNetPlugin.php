<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 **/

 require_once(dirname(__FILE__)."/config.php");

 
 /**
  * HERE THE PLUGIN HAS TO SUBSCRIBE TO THE PLUGIN MANAGER 
  */
 
 //PluginManager::plugin_subscribe("DemoPlugin");

/**
 * Class describing a single plugin. Every plugin must extend TagontoNetPlugin
 */
abstract class TagontoNetPlugin {
	protected $_name; 
	protected $_weburl;
	protected $_description;
	protected $_order;
	protected $_APIKEY;
	protected $_UNIQUE_NUMBER;

	/**
	 * Should be reimplemented!
	 */
	public function __construct() {
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
	}
	
	
	public function getLogo() {
		return $this->_logo;
	}
	

	
	public function getName() {
		return $this->_name;
	}
	
	public function getWebURL() {
		return $this->_weburl;
	}
	
	
	public function getOrder() {
		return $this->_order;
	}
	
	
	public abstract function getServices();
	
	public function getDescription() {
		return $this->_description;
	}
	
 
	//Plugins able to retrieve resources related to tags have to implement this
	//@return Array of Resources
	//public function getResourcesForTag($tag)

	//Plugins able to retrieve related tags have to implement this
	//@return Array of Tags
	//public function getRelatedTags($tag)	
	
}
?>