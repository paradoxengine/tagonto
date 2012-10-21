<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 **/
  /**
  * THIS IS A SAMPLE PHP CONFIG FOR THE STANDARD PLUGIN
  * YOU HAVE TO IMPLEMENT IT FOR EVERY NEW PLUGIN
  */
  
/**
 * CORE CONFIGURATION, NOT TO BE EDITED BY THE USER
 */ 
global $PluginConf; 
$UNIQUE_NUMBER = "TAGTHE1675623123";
//PLUGIN NAME HAS TO BE THE VERY SAME NAME OF THE PLUGIN CLASS, CASE SENSITIVE
$PluginConf[$UNIQUE_NUMBER]['name'] = "TagTheNet";
$PluginConf[$UNIQUE_NUMBER]['weburl'] = "http://www.tagthe.net";

/**
 * VARIABLES, TO BE EDITED BY THE USER
 */

 //$PluginConf[$UNIQUE_NUMBER]['APIKEY'] = "";
  
  
/*************
* REQUIREMENTS
*************/
  
// @require curl,libcurl,Zend Framework
 
 
 //Will require libcurl installed and curl support on php and will make use of the Zend Framework
 //for json decoding
 
  
?>
