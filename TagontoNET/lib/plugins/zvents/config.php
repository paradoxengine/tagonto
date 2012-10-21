<?php
/**
 * Created on 11/sep/07
 * @author Claudio Criscione
 * TagontoNET
 * 
 * CONFIG FILE FOR THE ZVENTS PLUGIN
 * 
 **/
 
/**
 * DO NOT EDIT
 */ 
global $PluginConf; 
$UNIQUE_NUMBER = "ZVE165012";
$PluginConf[$UNIQUE_NUMBER]['name'] = "Zvents";
$PluginConf[$UNIQUE_NUMBER]['weburl'] = "http://www.zvents.com";
$PluginConf[$UNIQUE_NUMBER]['order'] = '40';
$PluginConf[$UNIQUE_NUMBER]['description'] = <<<EODescription
Zvents is a website devoted to the management and publishing of events.
EODescription;
$PluginConf[$UNIQUE_NUMBER]['logo'] = 'http://images.zvents.com/images/zlogo.gif';


/**
 * EDIT FROM HERE BELOW
 */

 //Zvents API: you can obtain the API registering at zvents and getting to Update your profile.
$PluginConf[$UNIQUE_NUMBER]['APIKEY'] = "JFAVVBAMWFKIOPOJMYDWBTDGTNVEBXQJYKCQXFQDVWQIXEXCIHRQHFRYRUPQLJCE";

  
/*************
* REQUIREMENTS
*************/
  
  //@require libcurl
?>
