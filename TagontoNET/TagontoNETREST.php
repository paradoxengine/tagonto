<?php
/**
 * RESTful-like interface for TagontoNET
 * @author Claudio Criscione
 */

//MESSAGE CONSTANTS

$RESTMESSAGES['PLUGIN_NOT_FOUND_ERROR'] = 'Plugin not found';
$RESTMESSAGES['ERROR_LEVEL_FATAL'] = 'fatal';
$RESTMESSAGES['NO_RESULT_FOUND'] = 'No results found';
 
 //Init Tagontonet
 require_once('Tagontonet.php');

 $tnet = new TagontoNET();
 
 //Analyze request
 $Request = $_GET['r'];
 $TagLabel = $_GET['tag'];
 $Plugin = $_GET['pl'];
 $URI = $_GET['uri'];
 
 $tag = new TAG($TagLabel);
 
 //Getting the document on the right mood
 header("Content-Type: application/xml");
 	 		
 switch($Request) {
 	case 'GetFriends':
		//Check if a specific plugin has been requested
 		$Plugin = $_GET['pl'];
 		getFriends($tnet, $tag, $Plugin);
 		break;
 		
 	case 'GetResources':
 		//Check if a specific plugin has been requested
 		tagRes($tnet, $tag, $Plugin);
 		break;
 		
 	case 'ListPlugins':
 		listPlugins($tnet);
 		break;
 	
 	case 'ExtractTags':
 		extractTags($tnet,$URI);
 		break;
 	
 	default:
 		break; //TODO return an "unknown command" message
 }
 
 /**
  * Will print an XML document with all the related tags 
  *
  * @param STRING $Plugin plugin name
  * @param TAG $Tag tag object
  * @param TagontoNET $tnet
  */
 function getFriends($tnet, $tag, $Plugin = "") {
 	$RelTags = $tnet->getRelatedTags($tag,$Plugin);

 	//Creating the XML and outputting
 	$doc = new DomDocument('1.0');
 	$doc->formatOutput = true;
 	$root = $doc->createElement('tagonto');
 	$root->setAttribute('requestSatisfied','true');
 	$root = $doc->appendChild($root);
 	
 	if(empty($RelTags)) {
 		$xml_string = $doc->saveXML();
		echo $xml_string;
		return;
 	} 
	
 	foreach($RelTags as $Tag) {
 		//Main tag element
 		$tag = $doc->createElement('tag');
 		$tag->setAttribute('label',$Tag->getLabel());
 		$tag = $root->appendChild($tag);
 		
		$source = $doc->createElement('source');
	    $source = $tag->appendChild($source);
		$value = $doc->createTextNode( $Tag->getContext());
    	$value = $source->appendChild($value);
 		}
 		
	//Output xml
	$xml_string = $doc->saveXML();
	echo $xml_string;
 }
 
 /**
  * Will echo an XML document containing all the resources for a given tag
  *
  * @param STRING $Plugin pluginname
  * @param TAG $Tag tag object
  * @param TagontoNET $tnet
  */
 function tagRes($tnet, $tag, $Plugin = "") {

 	global $RESTMESSAGES;
 
 	if(!empty($Plugin) && !in_array($Plugin,$tnet->getPluginListArray())) {
 		generateErrorAndDie($RESTMESSAGES['PLUGIN_NOT_FOUND_ERROR'],$RESTMESSAGES['ERROR_LEVEL_FATAL']); 
 	}
 	$Res = $tnet->getResourcesForTag($tag,$Plugin);
 	
 	//TODO no results!
 	if(empty($Res)) {
 		generateErrorAndDie($RESTMESSAGES['NO_RESULT_FOUND']); 
 	}
 	
 	//Creating the XML and outputting
 	$doc = new DomDocument('1.0');
 	$doc->preserveWhiteSpace = false;
 	$doc->formatOutput = true;
 	
 	$root = $doc->createElement('tagonto');
 	$root->setAttribute('requestSatisfied','true');
 	$root = $doc->appendChild($root);

  	if(!empty($Res))
	 	foreach($Res as $Risorsa) {
	 		$risorsa = $doc->createElement('resource');
	 		$risorsa = $root->appendChild($risorsa);
			//Name
	 		$child = $doc->createElement('name');
		    $child = $risorsa->appendChild($child);
		    $value = $doc->createTextNode($Risorsa->getTitle());
		    $value = $child->appendChild($value);
		    //Url
		    $child = $doc->createElement('url');
		    $child = $risorsa->appendChild($child);
		    $value = $doc->createTextNode($Risorsa->getHomeUrl());
		    $value = $child->appendChild($value);
		    //Logo
		    $child = $doc->createElement('logo');
		    $child = $risorsa->appendChild($child);
		    $value = $doc->createTextNode( $Risorsa->getLogo());
		    $value = $child->appendChild($value);
	 	
	 		//Now, results
	 		$result = $doc->createElement('results');
			$result = $risorsa->appendChild($result);
			$tempresforchecks = $Risorsa->getResults();
			if(!empty($tempresforchecks)) {
		 		foreach($Risorsa->getResults() as $Risultato) {
		 			
		 			$singolorisultato = $doc->createElement('result');
		 			$singolorisultato = $result->appendChild($singolorisultato);
		 			
					//Creating results
					//URL
				    $child = $doc->createElement('url');
				    $child = $singolorisultato->appendChild($child);
				    $value = $doc->createTextNode( $Risultato->getUrl());
			    	$value = $child->appendChild($value);
			    	//SHOWN CONTENT
				    $child = $doc->createElement('showncontent');
				    $child = $singolorisultato->appendChild($child);
				    $value = $doc->createTextNode( $Risultato->getShownContent());
			    	$value = $child->appendChild($value);
			    	//TITLE
				    $child = $doc->createElement('title');
				    $child = $singolorisultato->appendChild($child);
				    $value = $doc->createTextNode( $Risultato->getTitle());
			    	$value = $child->appendChild($value);
			    	//DESC
				    $child = $doc->createElement('desc');
				    $child = $singolorisultato->appendChild($child);
				    $value = $doc->createTextNode( $Risultato->getDescription());
			    	$value = $child->appendChild($value);
			    	//TYPE
				    $child = $doc->createElement('type');
				    $child = $singolorisultato->appendChild($child);
				    $value = $doc->createTextNode( $Risultato->getContentType());
			    	$value = $child->appendChild($value);	    	
		 		}
			}
	 	}
	//Output xml
	$xml_string = $doc->saveXML();
	echo $xml_string;
 }
 
 /**
  * Will echo an XML document will all the available plugins
  * 
  * @param TagontoNET $tnet
  */
 function listPlugins($tnet) {

 	$plugins = $tnet->getPluginList();
 	
 	//Creating the XML and outputting
 	$doc = new DomDocument('1.0');
 	$doc->preserveWhiteSpace = false;
 	$doc->formatOutput = true;
 	$root = $doc->createElement('tagonto');
 	$root->setAttribute('requestSatisfied','true');
 	$root = $doc->appendChild($root);
 	

 	foreach($plugins as $Risorsa) {
 		//Main Plugin element
 		$risorsa = $doc->createElement('resource');
 		$risorsa = $root->appendChild($risorsa);
		//Name
 		$child = $doc->createElement('name');
	    $child = $risorsa->appendChild($child);
	    $value = $doc->createTextNode($Risorsa->getName());
	    $value = $child->appendChild($value);
	    //Url
	    $child = $doc->createElement('url');
	    $child = $risorsa->appendChild($child);
	    $value = $doc->createTextNode($Risorsa->getWebUrl());
	    $value = $child->appendChild($value);
	    //Logo
	    $child = $doc->createElement('logo');
	    $child = $risorsa->appendChild($child);
	    $value = $doc->createTextNode( $Risorsa->getLogo());
	    $value = $child->appendChild($value);
 		}

	//Output xml
	$xml_string = $doc->saveXML();
	echo $xml_string;
 }
 
 
 /**
 * generateError will create an error message for the REST interface, then stop execution
 **/
 function generateErrorAndDie($ErrorMessage,$ErrorType = 'standard') {
 	//Creating the XML and outputting
 	$doc = new DomDocument('1.0');
 	$doc->preserveWhiteSpace = false;
 	$doc->formatOutput = true;
 	$root = $doc->createElement('tagonto');
 	$root->setAttribute('requestSatisfied','false');
 	$root = $doc->appendChild($root);

 	//Creating Error element
 	$error = $doc->createElement('error');
 	$error->setAttribute('type',$ErrorType);
 	$value = $doc->createTextNode($ErrorMessage);
 	$error->appendChild($value);
 	$root->appendChild($error);

 	
 	$xml_string = $doc->saveXML();
	echo $xml_string;
	die();
 }
  

/**
* extractTags will output a list of tags given an URI
**/
function extractTags($tnet, $URI) {
	//Creating the XML and outputting
 	$doc = new DomDocument('1.0');
 	$doc->preserveWhiteSpace = false;
 	$doc->formatOutput = true;
 	$root = $doc->createElement('tagonto');
 	$root->setAttribute('requestSatisfied','true');
 	$root = $doc->appendChild($root);


 	
 	$xml_string = $doc->saveXML();
	echo $xml_string;
}


 
 ?>
 