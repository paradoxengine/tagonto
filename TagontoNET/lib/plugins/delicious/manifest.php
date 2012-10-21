<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 **/

 /**
  * HERE THE PLUGIN HAS TO SUBSCRIBE TO THE PLUGIN MANAGER 
  */
 
 require_once(dirname(__FILE__)."/lib/lastRSS.php");
 require_once(dirname(__FILE__)."/config.php");
 
 PluginManager::plugin_subscribe("DeliciousPlugin");
 
 
  class DeliciousPlugin extends TagontoNetPlugin {
 	
 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "DELI156061282CIOUS";	
		
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];
		
		if($DEBUGMODEON) 
		 echo "Delicious Plugin loaded<br>"; 
	}
	
	public function getServices()  {
		return array("Resources");
	}
	

	public function getResourcesForTag($tag) {
		global $PluginConf;
		$tagValue = $tag->getLabel();
		$Bundle = array();
		$Cachedir = $PluginConf[$this->_UNIQUE_NUMBER]['cachedir'];
		

		//We are using the lastRSS library to to the job
		$rss = new lastRSS;
	  	$rss->cache_dir = $Cachedir; 
  		$rss->cache_time = 360; // 3 minutes
		$rss->items_limit = 10;
  			
  		//Loading RSS
  		if ($Feed = $rss->get($PluginConf[$this->_UNIQUE_NUMBER]['rssurl'].$tagValue)) {
  			foreach($Feed['items'] as $Result) {
	  			$Res = new Result();
  				$Res->setTitle(html_entity_decode($Result['title']));
	  			$Res->setDescription(html_entity_decode($Result['description']));
	  			$Res->setUrl($Result['link']);
	  			$Res->setShownContent($Result['link']);
	  			$Res->setTagSearched($tag);
	  			$Res->setContentType(2);
	  			$Bundle[] = $Res;
  			}
  		}
  		else {
  			die ('Error: RSS file not found...');
  		}
		return $Bundle;
	}
}
 

?>
