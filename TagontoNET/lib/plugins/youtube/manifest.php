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
 
  //TODO  LEGGERE LA DOCUMENTAZIONE ERRORI 
  
 PluginManager::plugin_subscribe("YoutubePlugin");
 require_once(dirname(__FILE__)."/config.php");
		 
  class YoutubePlugin extends TagontoNetPlugin {
 	
 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "YOUTU16592182131";
		
		//Setting name and web address
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_logo = $PluginConf[$this->_UNIQUE_NUMBER]['logo'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];

	}
  public function getServices() {
		return array("Resources");	 
	 }
	
	 
  public function getResourcesForTag($tag) {

	 //Getting the tag label
	 $tagtosearch = $tag->getLabel();
	 
	//We simply query the right page and get back a JSON result.

	
	$tagendpoint = 'http://gdata.youtube.com/feeds/videos/-/';
	$tagendpoint .= $tagtosearch; 
	$tagendpoint .= '?alt=json&orderby=viewCount';


	$ch = curl_init($tagendpoint);		
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	$Jsonresult =curl_exec ($ch);
	curl_close ($ch);
	
	//TODO error handling
	
	//Decoding json answer
	$results = json_decode($Jsonresult);
	
	$entries = $results->feed->entry;

	//TODO better error handling
	if(empty($entries))
		return array();
	
	foreach($entries as $entry) {
		$TempRes = new Result(); 
		$TempRes->setUrl($entry->link[1]->href);
		$t = '$t';
		$TempRes->setTitle($entry->title->$t);
		$mediadesc = 'media$description';
		$mediagroup = 'media$group';
		$mediathumb = 'media$thumbnail';
		$TempRes->setDescription($entry->$mediagroup->$mediadesc->$t);
		
		$thumbarr = $entry->$mediagroup->$mediathumb;
		
		$TempRes->setShownContent($thumbarr[0]->url);
		
		$TempRes->setTagSearched($tag);
		$TempRes->setContentType(1); //text
		$ResourceBundle[] = $TempRes;
	}
  return $ResourceBundle;
 }
	
	
}
 
?>