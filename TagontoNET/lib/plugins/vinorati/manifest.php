<?php
/**
 * Created on 19/sep/07
 * @author Claudio Criscione
 * TagontoNET
 **/

 /**
  * HERE THE PLUGIN HAS TO SUBSCRIBE TO THE PLUGIN MANAGER 
  */
 
 require_once(dirname(__FILE__)."/config.php");
 
 PluginManager::plugin_subscribe("VinoratiPlugin");
 
 
  class VinoratiPlugin extends TagontoNetPlugin {
 	
  	private $_username;
  	private $_password;
 	
	public function __construct() {
		global $PluginConf;
		$this->_UNIQUE_NUMBER = "VINORA32162532";	
		
		$this->_name = $PluginConf[$this->_UNIQUE_NUMBER]['name'];
		$this->_weburl = $PluginConf[$this->_UNIQUE_NUMBER]['weburl'];
		$this->_description = $PluginConf[$this->_UNIQUE_NUMBER]['description'];
		$this->_order = $PluginConf[$this->_UNIQUE_NUMBER]['order'];
					
	}
	
	public function getServices()  {
		return array("Resources");
	}
	

  public function getResourcesForTag($tag) {
	 //Getting the tag label
	 $tagtosearch = $tag->getLabel();
		 
		 
	//Before everything else, we have to retrieve TAG ID from the tag name
	$tagendpoint = 'http://www.vinorati.com/tag/'; 
	$tagendpoint .= $tagtosearch;
	
	
	//retrieve the results page
	$ch = curl_init($tagendpoint);		
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	$resdoc =curl_exec ($ch);
	curl_close ($ch);

	if(empty($resdoc)) 
		return array();
	//Ok, we have to parse this document via regexp. i know i know, but no api was available.
	 preg_match_all("#<li[^\">]+class=\"textwine\"><a[^\"]+\"(.*)\">(.*)</a>[^<]+</li>#Ui",$resdoc,$entries);
  	//array 0 is the address, array 2 the name of the wine
 
	if(empty($entries[1]))
		return array();
	
	//Building the result set
	$mycounter = 0;
	foreach($entries[1] as $entry) {
		$TempRes = new Result(); 
		$TempRes->setUrl($entry);
		$TempRes->setTitle($entries[2][$mycounter]);
		$TempRes->setShownContent($entries[2][$mycounter]);
		$TempRes->setDescription("");
		$TempRes->setTagSearched($tag);
		$TempRes->setContentType(0); //text
		$ResourceBundle[] = $TempRes;
		$mycounter++;
	}
  return $ResourceBundle;
	}
 }

?>
