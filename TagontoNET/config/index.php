<?
/**
 * Main tagonto configurator: will create all the configurator files needed for Tagonto
 * 
 */

 
 //Setup of the main config script
 $toconfigure = $_GET['itemtoconfigure'];
 if(empty($toconfigure)) $toconfigure = 'chooser'; 
 
 $step = $_GET['step'];
 if(empty($step)) $step = 1;
 if(!is_numeric($step))
  	die('Step is not numeric!');
 
 
  	
 if($toconfigure == 'tagontolib') {
 	configureTagontoLib('tagontolib/tagonto_config.prop',$step);	
 }
 elseif($toconfigure == 'ontologyloader') {
 	include('tagontolib/ontologyLoader.php');	
 }
 else {
	 //Ok, it's chooser
 	include('chooser.php');
 }
 
 
 function configureTagontoLib($filename,$step) {
 	require_once('TagontoConfiguration.php');
 	//Loading tagontoconfiguration, a class to manage tagontolibconfiguration
 	$TagontoLibConfiguration = new TagontoConfiguration($filename);
 	
    require_once('tagontolib/step'.$step.'.php');
 }
 

?>