<?php
/**
 * This translator will OUTPUT a div containing all the code needed to let the user
 * discriminate between different mappings
 */ 

 require_once('../config.php');

 
 $tagtomap = $_GET['tag'];
 $tag = new Tag($tagtomap);

 $res = new ReasoningManager($TAGONTOLIB_MAPPING_ENDPOINT); 

 $log = new LoginManager();
 if($log->isLoggedin()) {
 	$usinfo = $log->getLoggedUserDetails();
 	$usid = $usinfo['id'];
 }
 else {
 	$usid = 1;
 }

 $mappingresults = $res->getConceptsByTag($ONTOLOGYID,$tag,$usid);
 
 
?>
<div id="disambiContainer">
<? if($mappingresults){ ?>
	<p>Choose the concept where to map the searched tag:</p>
<ul>
<?
 	foreach($mappingresults as $Concept) {
	 ?>
		<li class="disambiConcept">
	    <a onclick="choosedConcept('<? echo $Concept->getId();?>','<? echo $tagtomap ?>');" id="choose_concept"><? echo $Concept->getName(); ?></a>, URI: <span class="conceptUri"><? echo $Concept->getUri(); ?></span><br/>
		<!--  <span class="desc"></span><br/> -->
		</li>
		<?
	 } ?>
</ul>
<? }
else { ?>
<!-- no concepts found error -->
<p>No concept mapped with the given tag.<br/>
Logging in you can try to map the tag on an arbitrary ontology concept.</p>
<? }
 
	//Adding support for reinforcement mapping
	$logman = new LoginManager();
	
	//if the user is logged
	if($logman->isLoggedin()) {
		?>
		<p><a onclick="showAllConcepts('<? echo $tagtomap ?>');">Choose a different concept for the mapping of this tag</a></p>
<?	} ?> 	
	
</div>