<?php
/**
 * Created on 11/sep/07
 * @author Claudio Criscione
 * TagontoNET
 * 
 * CONFIG FILE FOR THE 43Things PLUGIN
 * 
 **/
 
/**
 * DO NOT EDIT
 */ 
global $PluginConf; 
$UNIQUE_NUMBER = "43T12381xsar124";
$PluginConf[$UNIQUE_NUMBER]['name'] = "43Things";
$PluginConf[$UNIQUE_NUMBER]['weburl'] = "http://www.43things.com";
$PluginConf[$UNIQUE_NUMBER]['order'] = '100';
$PluginConf[$UNIQUE_NUMBER]['description'] = <<<EODescription
People have known for years that making a list of goals is the best way to achieve them.
EODescription;
$PluginConf[$UNIQUE_NUMBER]['logo'] = 'http://www.43things.com/images/nav/logo-big.gif';


/**
 * EDIT FROM HERE BELOW
 */

 //You can obtain 43Things api registering @ www.43things.com and then going to 
 // http://www.43things.com/account/webservice_setup
$PluginConf[$UNIQUE_NUMBER]['APIKEY'] = '1559977@TauuLhW0tamJU';

  
/*************
* REQUIREMENTS
*************/
  
  //@require libcurl
?>
