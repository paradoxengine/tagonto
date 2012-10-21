<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 **/

 /**
  * HERE THE PLUGIN HAS TO SUBSCRIBE TO THE PLUGIN MANAGER 
  */
 
 require_once(dirname(__FILE__)."/config.php");
 
 PluginManager::plugin_subscribe("ConnoteaPlugin");
 
 
  class ConnoteaPlugin extends TagontoNetPlugin {
 	
  	private $_username;
  	private $_password;
 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "CONN11625234A";	
		
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];
		$this->_username = $PluginConf[$this->_UNIQUE_NUMBER]['username'];
		$this->_password = $PluginConf[$this->_UNIQUE_NUMBER]['password'];
						
	}
	
	public function getServices()  {
		return array("Resources");
	}
	

  public function getResourcesForTag($tag) {
	 //Getting the tag label
	 $tagtosearch = $tag->getLabel();
		 
		 
	$tagendpoint = 'http://www.connotea.org/data/bookmarks/tag/'; 
	$tagendpoint .= $tagtosearch;
	
	$ch = curl_init($tagendpoint);		
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	curl_setopt($ch, CURLOPT_HEADER, 0);
	curl_setopt($ch, CURLOPT_USERPWD,$this->_username.":".$this->_password);
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);	

	$xmldoc =curl_exec ($ch);
	curl_close ($ch);

	if(empty($xmldoc)) 
		return array();
	
	$doc = new DOMDocument();
	$doc->preserveWhiteSpace = false;
	//Loading the XML doc
	$doc->LoadXML($xmldoc);
	$xpath = new DOMXPath($doc);
		
	//registering the namespace
	$defnamespace = $doc->documentElement->lookupnamespaceURI("dcterms");
	$xpath->registerNamespace('dcterms',$defnamespace);

	//Extracting result information
	$query = '//dcterms:URI';
	$entries = $xpath->query($query);

	if($entries->length == 0)
		return array();
	
	
	//We obtained the main entry for each resul. now we have to check 	
	foreach($entries as $entry) {
		$TempRes = new Result(); 
		$TempRes->setUrl($entry->getElementsByTagName('link')->item(0)->nodeValue);
		$TempRes->setTitle($entry->getElementsByTagName('title')->item(0)->nodeValue);
		$TempRes->setShownContent($entry->getElementsByTagName('link')->item(0)->nodeValue);
		$TempRes->setDescription("");
		$TempRes->setTagSearched($tag);
		$TempRes->setContentType(0); //text
		$ResourceBundle[] = $TempRes;
	}
  return $ResourceBundle;
	}
	
	
	
	public function getRelatedTags($tag) {
		$taglabel = $tag->getLabel();
		//Retrieving related tags
		 //Getting the tag label
		 $tagtosearch = $tag->getLabel();
			 
		//To retrieve tag friends, we have to search for a tag and then 
		//retrieve all the tags listed in the same document			 
		$tagendpoint = 'http://www.connotea.org/data/bookmarks/tag/'; 
		$tagendpoint .= $tagtosearch;
		
		$ch = curl_init($tagendpoint);		
		curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
		curl_setopt($ch, CURLOPT_HEADER, 0);
		curl_setopt($ch, CURLOPT_USERPWD,$this->_username.":".$this->_password);
	    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);	
	
		$xmldoc =curl_exec ($ch);
		curl_close ($ch);
	
		if(empty($xmldoc)) 
			return array();
		
		$doc = new DOMDocument();
		$doc->preserveWhiteSpace = false;
		//Loading the XML doc
		$doc->LoadXML($xmldoc);
		$xpath = new DOMXPath($doc);
		
		//registering the namespaces, both the dcterms one and the default one
		$defnamespace = $doc->documentElement->lookupnamespaceURI("dcterms");
		$xpath->registerNamespace('dcterms',$defnamespace);
		$xpath->registerNamespace('def',"http://www.connotea.org/2005/01/schema#");
		//$defnamespace2 = $doc->documentElement->lookupnamespaceURI("dcterms");
		
		
		//Extracting result information
		$query = '//dcterms:URI/def:tag';
		$entries = $xpath->query($query);
		
		//No tag found
		if($entries->length == 0) {
			return array();
			}

			$TempStorage = array();
		foreach($entries as $eletagtag) {
				if(!in_array($eletagtag->nodeValue,$TempStorage))
					$TempStorage[] = $eletagtag->nodeValue;
			}
			
		foreach($TempStorage as $taglabel) {
			$ResourceBundle[] = new Tag($taglabel);
		}
			//TODO number of matches?
			//Building return array
			return $ResourceBundle;		
	}
	
	
 }

?>
