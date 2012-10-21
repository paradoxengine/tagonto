<?php
/**
 * Created on 11/sep/07
 * @author Claudio Criscione
 * TagontoNET
 * 
 * CONFIG FILE FOR THE YOUTUBE PLUGIN
 * Youtube has a great API, able to return search results for single tags without API KEY or other useless crap
 * in JSon format.
 * 
 **/
 
/**
 * DO NOT EDIT
 */ 
global $PluginConf; 
$UNIQUE_NUMBER = "YOUTU16592182131";
$PluginConf[$UNIQUE_NUMBER]['name'] = "Youtube";
$PluginConf[$UNIQUE_NUMBER]['weburl'] = "http://www.youtube.com";
$PluginConf[$UNIQUE_NUMBER]['order'] = '40';
$PluginConf[$UNIQUE_NUMBER]['description'] = <<<EODescription
The ubiquitus video sharing site
EODescription;
$PluginConf[$UNIQUE_NUMBER]['logo'] = 'http://www.youtube.com/img/pic_youtubelogo_123x63.gif';



  
/*************
* REQUIREMENTS
*************/
  
  //@require libcurl
  //@require PHP >= 5.2 or with JSON support
?>
