<?php
/**
 * Created on 09/ago/07
 * @author Claudio Criscione
 * TagontoNET
 **/
  /**
  */
  
/**
 * CORE CONFIGURATION, NOT TO BE EDITED BY THE USER
 */ 
global $PluginConf; 
$UNIQUE_NUMBER = "DELI156061282CIOUS";
$PluginConf[$UNIQUE_NUMBER]['name'] = "Delicious";
$PluginConf[$UNIQUE_NUMBER]['weburl'] = "http://www.del.icio.us";
$PluginConf[$UNIQUE_NUMBER]['order'] = '10';
$PluginConf[$UNIQUE_NUMBER]['description'] = <<<EODescription
		Delicious is a pupular social bookmarking website.
EODescription;
$PluginConf[$UNIQUE_NUMBER]['logo'] = 'http://images.del.icio.us/static/img/delicious.42px.gif';
/**
 * VARIABLES, TO BE EDITED BY THE USER
 */

$PluginConf[$UNIQUE_NUMBER]['APIKEY'] = "";
$PluginConf[$UNIQUE_NUMBER]['cachedir'] = "cache"; //Absolute or relative path
$PluginConf[$UNIQUE_NUMBER]['rssurl'] = "http://del.icio.us/rss/tag/"; //RSS feed URL, default is ok

  
/*************
* REQUIREMENTS
*************/
   
 //Requires the cache directory to be WORLD WRITABLE
  
 //Please note that this plugin uses html_entity_decode on title and description: this is a factual and likely security risk
 //introduced to have "nice results". Make sure you remove it in production.
 
?>
