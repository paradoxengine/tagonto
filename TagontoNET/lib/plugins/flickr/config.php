<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 * 
 * CONFIG FILE FOR THE FLICKR PLUGIN
 * 
 **/
 
/**
 * DO NOT EDIT
 */ 
global $PluginConf; 
$UNIQUE_NUMBER = "FLCKR123012582";
$PluginConf[$UNIQUE_NUMBER]['name'] = "Flickr";
$PluginConf[$UNIQUE_NUMBER]['weburl'] = "http://www.flickr.com";
$PluginConf[$UNIQUE_NUMBER]['order'] = '20';
$PluginConf[$UNIQUE_NUMBER]['description'] = <<<EODescription
		Flickr is a popular photo sharing website.
EODescription;
$PluginConf[$UNIQUE_NUMBER]['logo'] = 'http://l.yimg.com/www.flickr.com/images/flickr_logo_gamma.gif.v1.5.10';


/**
 * EDIT FROM HERE BELOW
 */

 //Flickr API: you can obtain the API @ www.flickr.com/api
$PluginConf[$UNIQUE_NUMBER]['APIKEY'] = "2f9eff7316424ba51f87ce539477bac1";
  
  
  
/*************
* REQUIREMENTS
*************/
  
// @require PEAR/Http_Request
// @require PEAR/DB

// Requires both PEAR plugins to be installed: DB and HTTP_Request
  
  
?>
