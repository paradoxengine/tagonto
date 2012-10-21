<?php
/**
* XSL translator will apply an XSL translation to an XML file
* It is used to format the output from TagontoNET plugins
* @author Silvia Bindelli
*/

require_once('../config.php');
//TODO some security checks here
 $url = urldecode($_GET['urltoget']);

//Fetch the remote XML
 $ch = curl_init($url);
 curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
 $TagontoResults=curl_exec ($ch);
 curl_close ($ch);

//Fetch the XSL file
 $ch = curl_init($TAGONTONET_XSL);
 curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
 $Style=curl_exec($ch);
 curl_close ($ch);

// Load the XML source
 $xml = new DOMDocument; 
 $xml->loadXML($TagontoResults);


// Load the XSL file	
 $xsl = new DOMDocument;
 $xsl->loadXML($Style);

// Configure the transformer
 $proc = new XSLTProcessor();
 $proc->importStyleSheet($xsl); // attach the xsl rules
 echo $proc->transformToXML($xml); //output the results

?>