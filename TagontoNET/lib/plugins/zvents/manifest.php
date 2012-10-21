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
 
  //TODO  LEGGERE LA DOCUMENTAZIONE ERRORI http://www.zvents.com/api_doc/error
  
 PluginManager::plugin_subscribe("ZventsPlugin");
 require_once(dirname(__FILE__)."/config.php");
		 
  class ZventsPlugin extends TagontoNetPlugin {
 	
 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "ZVE165012";
		
		//Setting name and web address
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_logo = $PluginConf[$this->_UNIQUE_NUMBER]['logo'];
		$this->_APIKEY =  $PluginConf[$this->_UNIQUE_NUMBER]['APIKEY'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];
		
	}
	 public function getServices() {
		return array("Resources");	 
	 }
	
	 
	public function getResourcesForTag($tag) {

		 //Getting the tag label
		 $tagtosearch = $tag->getLabel();
		 
		 
	//Before everything else, we have to retrieve TAG ID from the tag name
	$tagbynameendpoint = 'http://www.zvents.com/rest/tag_by_name/'; 
	$tagbynameendpoint .= $tagtosearch;
	$tagbynameendpoint .= '?key='.$this->_APIKEY;


	$ch = curl_init($tagbynameendpoint);		
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	$xmldoc =curl_exec ($ch);
	curl_close ($ch);

	if(empty($xmldoc)) 
		return array();
	
	$doc = new DOMDocument();
	$doc->preserveWhiteSpace = false;
	//Loading the XML doc
	$doc->LoadXML($xmldoc);
	$xpath = new DOMXPath($doc);
	
	//Extracting tag information
	$query = '//tag';
	$entries = $xpath->query($query);
	
	if($entries->length == 0)
		return array();
	
		
	$tagid = $entries->item(0)->getAttributeNode('id')->nodeValue;
	
	//We can now query for related events
	
	$taginfoendpoint = 'http://www.zvents.com/rest/tag_events/'; 
	$taginfoendpoint .= $tagid; //query has to be done using the tag
	$taginfoendpoint .= '?key='.$this->_APIKEY;
	
	//Curl init
	$ch = curl_init($taginfoendpoint);		
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	$xmldoc =curl_exec ($ch);
	curl_close ($ch);
	//DOC init
	$doc = new DOMDocument();
	$doc->preserveWhiteSpace = false;
	//Loading the XML doc
	$doc->LoadXML($xmldoc);
	$xpath = new DOMXPath($doc);
	
	//Extracting tag information
	$query = '//event';
	$entries = $xpath->query($query);
	
	//TODO better error handling
	if(empty($entries))
		return array();
	
	foreach($entries as $entry) {
		$TempRes = new Result(); 
		$TempRes->setUrl($entry->getElementsByTagName('link')->item(0)->nodeValue);
		$TempRes->setTitle($entry->getElementsByTagName('name')->item(0)->nodeValue);
		$TempRes->setShownContent($entry->getElementsByTagName('link')->item(0)->nodeValue);
		$TempRes->setDescription($entry->getElementsByTagName('description')->item(0)->nodeValue);
		$TempRes->setTagSearched($tag);
		$TempRes->setContentType(0); //text
		$ResourceBundle[] = $TempRes;
	}
  return $ResourceBundle;
 }
	
	
}
 
?>