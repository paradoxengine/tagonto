<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 ***********************
 * FLICKR PLUGIN
 * MANIFEST FILE
 ***********************
 * 
 **/
 
 /**
  * HERE THE PLUGIN HAS TO SUBSCRIBE TO THE PLUGIN MANAGER 
  */
 
 PluginManager::plugin_subscribe("FlickrPlugin");
 require_once( dirname(__FILE__). "/lib/phpFlickr.php");
 require_once(dirname(__FILE__)."/config.php");
		 
  class FlickrPlugin extends TagontoNetPlugin {
 	
  	var $FlickrLib; 

 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "FLCKR123012582";
		
		//Setting name and web address
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_logo = $PluginConf[$this->_UNIQUE_NUMBER]['logo'];
		$this->_APIKEY =  $PluginConf[$this->_UNIQUE_NUMBER]['APIKEY'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];		
		 
		$this->FlickrLib = new phpFlickr($this->_APIKEY);
	}
	 public function getServices() {
		return array("Resources","Friends");	 
	 }
	
	 
	public function getResourcesForTag($tag) {
		if($DEBUGMODEON) 
		 echo "Entered getresourcesForTag, flickr plugin<br>"; 

		 //Getting the tag label
		 $tagtosearch = $tag->getLabel();
		 
		//Retrieving photo
		$Photo = $this->FlickrLib->photos_search(array("tags"=>$tagtosearch,"sort"=>'relevance','per_page'=>'30'));

		//if no picture found...
		if(count($Photo['photo']) == 0)
			return array();
			
		//Building resource objects
		foreach($Photo["photo"] as $singlepic) {
			$TempRes = new Result(); 
			$TempRes->setUrl("http://www.flickr.com/photos/" . $singlepic['owner'] . "/" . $singlepic['id']);
			$TempRes->setTitle($singlepic["title"]);
			$TempRes->setShownContent($this->FlickrLib->buildPhotoURL($singlepic, "Square"));
			$TempRes->setDescription("");
			$TempRes->setTagSearched($tag);
			$TempRes->setContentType(1); //images
			$ResourceBundle[] = $TempRes;
		}
		return $ResourceBundle;
	}
	
	public function getRelatedTags($tag) {
		$taglabel = $tag->getLabel();
	//Retrieving related tags
		$Tags = $this->FlickrLib->tags_getRelated($taglabel);
		foreach($Tags['tag'] as $tag) {
			$ResourceBundle[] = new Tag($tag);
		}
		//TODO number of matches?
		//Building return array
		return $ResourceBundle;		
	}
	
	
}
 
?>