<?php
/**
 * Created on 09/ago/07
 *
 **/

/**
 * INCLUDES
 */
require_once("lib/PluginManager.php");
require_once("lib/Resource.php");
require_once("lib/Result.php");
require_once("lib/Tag.php");

/**
 * Main TagontoNET class
 */
class TagontoNET {
	
  static public $PluginManager; 
 	
 	
 
  private static function getPluginManager() {
  	global $DEBUGMODEON;
  	if($DEBUGMODEON) echo "Retrieving Plugin Manager<br>";
  	if(empty(self::$PluginManager))
  	  self::$PluginManager = new PluginManager();
  	 return self::$PluginManager;
  }

/**
 * @return Array of resources containing a list of plugins
 */
 public function getPluginList() {
 	$Pl = $this->getPluginManager();
 	$pllist =  $Pl->getPluginList();
     usort($pllist, "pluginSorter");
 	 return $pllist;
 }
 	
 
/**
 * @return Array of string containing a list of plugins
 */
 public function getPluginListArray() {
 	$Pl = $this->getPluginManager();
 	$Pllist = $Pl->getPluginList();
 	foreach($Pllist as $Plugin) {
 		$retvar[] = $Plugin->getName();
 	}
 	return $retvar;
 }
 	
 
 /**
  * getResourcesForTag will return an array of resources containing all the informations
  * the plugins can supply for a given tag 
  *
  * @param TAG $tag
  * @return array Resource bundle
  */
 public function getResourcesForTag($tag = "",$Plugin = "") {
 	//Transforming tag here
 	if(empty($tag))
 		//TODO ERROR HANDLING
 		return null;
 	$Plug = self::getPluginManager();

 	if(!empty($Plugin)) {
 		$Bundle = $Plug->getResourcesForTag($tag,$Plugin);
 	}
 	else {
 		$Bundle = $Plug->AP_getResourcesForTag($tag);	
 	}
 	return $Bundle;
 }
	

/**
 * Will return an array where keys are plugins and values arrays
 *
 * @param TAG $tag
 * @return ARRAY { plugin=> {related tags} }
 */
public function getRelatedTags($tag = "",$Plugin = "") {
 	if(empty($tag))
 		//TODO ERROR HANDLING
 		return null;
 	
 	$Plug = self::getPluginManager();
 	
 	if(!empty($Plugin)) {
 		$Bundle = $Plug->getRelatedTags($tag,$Plugin);
 		}
 	else {
 		$Bundle = $Plug->AP_getRelatedTags($tag);
 	}
 	return $Bundle;	 
	}
}


function pluginSorter($pl1,$pl2) {
	if($pl1->getOrder() == $pl2->getOrder())
		return 0;
	elseif($pl1->getOrder() > $pl2->getOrder())
		return 1;
	else
		return -1;
}
?>