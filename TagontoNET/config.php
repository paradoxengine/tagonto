<?php

//NEEDED
//php5-xsl

 require_once dirname(__FILE__).'/Tagontonet.php';
 require_once dirname(__FILE__).'/includes/functions.php';
 require_once dirname(__FILE__).'/classes/ReasoningManager.php';
 require_once dirname(__FILE__).'/classes/LoginManager.php'; //starting session here
 require_once dirname(__FILE__).'/classes/Ontology.php';
 require_once dirname(__FILE__).'/classes/OntologyConcept.php';

 	//Where the NETREST resides
  $TAGONTONET_REST_AJAX_URL = "http://192.168.66.3/tagonto/TagontoNETREST.php?";
    //Where the XSL for NETREST resides
  $TAGONTONET_XSL = "http://192.168.66.3/tagonto/XSL/resultsRendererPhp.php";
    //Where the Tagontolib endpoint resides
  $TAGONTOLIB_MAPPING_ENDPOINT = 'http://192.168.66.1:9000/ontologies/'; //NOTICE THE ENDING /
  
  $TAGONTONET_XSL_TRANSLATOR = "http://192.168.66.3/tagonto/ajax/xsltranslator.php";
  
  //TODO
  //The ontologyid is now static. Go on and set it as dynamic!
  $ONTOLOGYID = 1;
  
  
  //We will enstabilish a connection here
  $TAGONTO_DB = 'tagonto';
  $TAGONTO_DB_USERNAME = 'tagonto';
  $TAGONTO_DB_PASSWORD = 'tagonto';
  $TAGONTO_DB_HOST = '192.168.66.3';
  $TAGONTO_DB_PORT = '3306';
  
  //TODO error handling here!
  mysql_connect($TAGONTO_DB_HOST,$TAGONTO_DB_USERNAME,$TAGONTO_DB_PASSWORD);
  mysql_select_db($TAGONTO_DB);
  
  
?>