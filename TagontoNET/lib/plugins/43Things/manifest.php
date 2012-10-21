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
  
 PluginManager::plugin_subscribe("FortyThreeThingsPlugin");
 require_once(dirname(__FILE__)."/config.php");
		 
  class FortyThreeThingsPlugin extends TagontoNetPlugin {
 	
 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "43T12381xsar124";
		
		//Setting name and web address
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_logo = $PluginConf[$this->_UNIQUE_NUMBER]['logo'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];
		$this->_APIKEY =  $PluginConf[$this->_UNIQUE_NUMBER]['APIKEY'];

	}
	 public function getServices() {
		return array("Resources");	 
	 }
	
	 
	public function getResourcesForTag($tag) {

	//Getting the tag label
	$tagtosearch = $tag->getLabel();
	
		 
	//Before everything else, we have to retrieve TAG ID from the tag name
	$tagresendpoint = 'http://www.43things.com/service/get_tags_goals?id='; 
	$tagresendpoint .= $tagtosearch;
	$tagresendpoint .= '&api_key='.$this->_APIKEY;

	$ch = curl_init($tagresendpoint);		
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	$xmldoc =curl_exec ($ch);
	curl_close ($ch);

	//43Things will return an empty page on no match
	if(empty($xmldoc))
		return array();
	
	$doc = new DOMDocument();
	$doc->preserveWhiteSpace = false;
	//Loading the XML doc
	$doc->LoadXML($xmldoc);

	$xpath = new DOMXPath($doc);
	//WE HAVE TO SETUP THINGS TO USE THE DEFAULT NAMESPACE
	$defnamespace = $doc->documentElement->lookupnamespaceURI(NULL);
	$xpath->registerNamespace('m',$defnamespace);

	//Extracting tag information
	$query = '/m:feed/m:goal';
	$entries = $xpath->query($query);

	if(empty($entries))
		return array();

	foreach($entries as $entry) {
		$TempRes = new Result(); 
		$TempRes->setUrl($entry->getElementsByTagName('link')->item(0)->getAttribute('href'));
		$TempRes->setTitle($entry->getElementsByTagName('name')->item(0)->nodeValue);
		$TempRes->setShownContent($TempRes->getUrl());
		$TempRes->setDescription('');
		$TempRes->setTagSearched($tag);
		$TempRes->setContentType(0); //text
		$ResourceBundle[] = $TempRes;
	}
	
  return $ResourceBundle;
 }
	
	
}
 
?>