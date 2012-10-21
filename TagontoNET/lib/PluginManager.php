<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 **/
 
 require_once("plugins/TagontoNetPlugin.php"); //PLUGIN SUPERCLASS
 
 /**
  * Class to manage plugins
  */
 class PluginManager {
 
 	private static $_PluginsAvailable; //Array containing the plugins found
 	private $_PluginsLoaded; //Array with the loaded plugins
 
 
 	public function __construct() {
 		 		
 		global $DEBUGMODEON;
 		if($DEBUGMODEON)
 			echo "Entering Plugin Manager constructor <br>";
 		$Plugins = array();
 	
 	//Scanning the plugins dir, getting dir names so we can look for the manifests
 	    $curdir = dirname(__FILE__);
		$plugindir = $curdir.DIRECTORY_SEPARATOR.'plugins';
		
 	    $scanlisting = scandir($plugindir);
 
 		$plugindirfound = array();
		 foreach($scanlisting as $key => $value){
   	    	 if (is_dir("$plugindir/$value") == true && $value != '.' && $value != '..'){
         		$plugindirfound[] = $value;
  	 		}
 		}
 	//Checking for manifest file existance so we can open it
 		foreach($plugindirfound as $SPluginDir)
 			if(file_exists("$plugindir/$SPluginDir/manifest.php"))
	 			require_once("$plugindir/$SPluginDir/manifest.php");	
 
 		//If we were able to load at least one plugin
 		if(count(self::$_PluginsAvailable)) {
		 	//Now the plugins SHOULD have subscribed the PluginManager. Let's load them all
 				foreach(self::$_PluginsAvailable as $PluginName) {
 					$this->_PluginsLoaded[] = new $PluginName();
	 			}
 		}
 		else
 			//TODO SOMETHING HERE

 		if($DEBUGMODEON) echo "NO PLUGIN FOUND<br>";	
 	}
 
  /**
   * This function will be used by plugins to let the Manager know they exists
   */
 	public static function plugin_subscribe($ClassName) {
 		$arr = self::$_PluginsAvailable;
 		$arr[] = $ClassName;
 		self::$_PluginsAvailable = $arr;
 	}
 
 	public static function get_available_plugins() {
 		return self::_PluginsAvailable;
 	}
 
 	/**
 	 * @return List containing the list of loaded plugins
 	 */
 	public function getPluginList() {
 		return $this->_PluginsLoaded;
 	}
 	
 	/**
 	 * AP (All Plugin) getResourcesForTag will return a bundle of resources querying every
 	 * plugin exposing the getResourcesForTag method
 	 *
 	 * @param TAG $tag
 	 */
	public function AP_getResourcesForTag($tag = "") {
		if(empty($tag)) return null;
		foreach($this->_PluginsLoaded as $Plugin) {
			if(method_exists($Plugin, "getResourcesForTag")) {
				 $res = new Resource();
				 $res->setTitle($Plugin->getName());
				 $res->setLogo($Plugin->getLogo());
				 $res->setDescription($Plugin->getDescription());
				 $res->setHomeUrl($Plugin->getWebURL());
				 $res->setResults($Plugin->getResourcesForTag($tag));
				 $Bundle[] = $res;
				}
		}
		return $Bundle;
	}

	public function getResourcesForTag($tag = "",$pluginname = "") {
		if(empty($tag) || empty($pluginname)) return null;
		foreach($this->_PluginsLoaded as $Plugin) {
			if(method_exists($Plugin, "getResourcesForTag") && $Plugin->getName() == $pluginname) {
				 $res = new Resource();
				 $res->setTitle($Plugin->getName());
				 $res->setLogo($Plugin->getLogo());
				 $res->setDescription($Plugin->getDescription());
				 $res->setHomeUrl($Plugin->getWebURL());
				 $res->setResults($Plugin->getResourcesForTag($tag));
				 $Bundle[] = $res;
				}
		}
		return $Bundle;
	}
	
	
	/**
	* this function will return a bundle of tags querying only the selected Plugin
	**/
	public function getRelatedTags($tag = "", $pluginname = "") {
		$Bundle = array();
		if(empty($tag) || empty($pluginname)) return null;
		foreach($this->_PluginsLoaded as $Plugin) {
			if(method_exists($Plugin, "getRelatedTags") && $Plugin->getName() == $pluginname) {
				 $Plugs = $Plugin->getRelatedTags($tag);
				 foreach($Plugs as &$pl) {
					$pl->setContext($Plugin->getName());
				 }
				 $Bundle = $Plugs;
				}
			}
			return $Bundle;	
	}
	
	
	/**
	 * AP (All Plugin) getRelatedTags will return a bundle of tags querying every
 	 * plugin exposing the getRelatedTags method
 	 *
 	 * @param TAG $tag
 	 **/
	public function AP_getRelatedTags($tag = "") {
		$Bundle = array();
		if(empty($tag)) return null;
		foreach($this->_PluginsLoaded as $Plugin) {
			if(method_exists($Plugin, "getRelatedTags")) { 
				$Plugs = $Plugin->getRelatedTags($tag);
				if(!empty($Plugs)) {
					foreach($Plugs as &$pl) {
						$pl->setContext($Plugin->getName());
					}
					$Bundle = array_merge($Plugs,$Bundle);
				}
				
			}
		}
		return $Bundle;
	}
 }
?>