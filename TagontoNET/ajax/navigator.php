<?php
	
/**
 * This translator will OUTPUT a div containing all the code needed to show the Concept selected
 * by the user for the mapping, together with its linked concepts to allow navigability
 */ 

  require_once('../config.php');

	

	
	//Getting parameters
	$conceptIDToShow = $_GET['concept'];
	$tag = $_GET['tag'];
	
	//to avoid setting tag to map if the caller function is navigationConcept
	$isOnlyNavigation=$_GET['noTag'];
	
	//Creating classes
	$res = new ReasoningManager($TAGONTOLIB_MAPPING_ENDPOINT);
	$Ontology = new Ontology($ONTOLOGYID);

	//Retrieving the mapped concept by ID
	$mappedConcept = $Ontology->getConceptById($conceptIDToShow);

	//Adding support for reinforcement mapping
	$logman = new LoginManager();
	//if the user is logged
	if($logman->isLoggedin()) {
		$user = $logman->getLoggedUserDetails();
		if (!($isOnlyNavigation)){
			$res->mapTagOnConcept(new Tag($tag),$mappedConcept,$user['id'],$user['significance'],$ONTOLOGYID);
		}	
	}

	//Error handling
	if($mappedConcept == null) {
		die('Mapped concept is null!');
	}
	?>

	<div id="ontologyResults">

	<p id="ontologyMappedConcept">
		<span id="concept"><? echo $mappedConcept->getName(); ?> </span>: 
		<span class="conceptUri"><? echo $mappedConcept->getUri(); ?> </span><br/> 
		
		<span class="instances">Instances: 
		<? $instances = $mappedConcept->getInstances();
		if (!empty($instances)){
		      foreach ($instances as $instance){
		?>
		      <a onclick="plugsAndConceptFromTag('<? echo str_replace('#','',strrchr ($instance, '#')) ?>');"><? echo str_replace('#','',strrchr ($instance, '#')); ?></a>,
	    	<? } 
	    } ?>			
	    </span><br/>
		
		
		<span class="tags">Tags: 
		<? $conceptTags = $res->getAllTagsByConcept($mappedConcept);
		   if($conceptTags)
		      foreach ($conceptTags as $tag=>$mapping){
		?>
		      <a onclick="plugsAndConceptFromTag('<? echo $tag ?>');"><? echo $tag; ?></a>,
	    	<? } ?>			
	    	</span><br/>
	</p>
	
	<?
	$relatedConcepts = $Ontology->getRelatedOntologyConceptsAndInstances($mappedConcept);
	if($relatedConcepts) { ?>
	
	<div id="relatedClassesContainer">
	<p class="boxTitle">Related Concepts</p>
	<?	foreach($relatedConcepts as $assocconceptid=>$dataarray) {
			?>
			<p class="linkedOntologyConcept">
				<span class="rel"><? echo str_replace('#','',strrchr ($dataarray['property'], '#')) ?></span>:
				<a onclick=" navigationConcept('<? echo $dataarray['concept']->getId();?>'); " ><? echo $dataarray['concept']->getName(); ?></a><br/>
				<!-- <span class="desc">Description of the linked ontology concept</span><br/> -->
				
				<!-- instances line -->
				<span class="instances">Instances:
				<? $instances = $dataarray['instances'];
				if(!empty($instances))
				   foreach ($instances as $instance){
						?>
						<a onclick="plugsAndConceptFromTag('<? echo str_replace('#','',strrchr ($instance, '#')) ?>');"><? echo  str_replace('#','',strrchr ($instance, '#')); ?></a>, 
					<? } ?>	
				</span><br/>
				
				<!-- tags line -->
				<span class="tags">Tags:
				<? $conceptTags = $res->getAllTagsByConcept($dataarray['concept']);
				if(!empty($conceptTags))
				   foreach ($conceptTags as $tag=>$mapping){
						?>
						<a onclick="plugsAndConceptFromTag('<? echo $tag ?>');"><? echo $tag; ?></a>, 
					<? } ?>	
				</span><br/>
				
			</p>
		<? } ?>
	</div>
	
	<? }	
	
	
	
	$superAndSubArray = $Ontology->getSuperAndSubclasses($mappedConcept);
	$superClasses = $superAndSubArray['superclasses'];
	$subClasses = $superAndSubArray['subclasses'];
	
	if(!empty($superClasses)) { ?>
	
	<div id="superClassesContainer">
	<p class="boxTitle">Super Concepts</p>	
	<? foreach($superClasses as $superConcept){  ?>
	
				<p class="linkedOntologyConcept">
				
				<a onclick=" navigationConcept('<? echo $superConcept->getId();?>'); " ><? echo $superConcept->getName(); ?></a><br/>
				<!-- <span class="desc">Description of the linked ontology concept</span><br/> -->
				
				<!-- tags line -->
				<span class="tags">Tags:
				<? $conceptTags = $res->getAllTagsByConcept($superConcept);
				if(!empty($conceptTags))
				   foreach ($conceptTags as $tag=>$mapping){
						?>
						<a onclick="plugsAndConceptFromTag('<? echo $tag ?>');"><? echo $tag; ?></a>, 
					<? } ?>	
				</span><br/>
				
			</p>
	<?	}
	
	?>
	</div>
	
	<? } 
		
	if(!empty($subClasses)) { ?>
	
	<div id="subClassesContainer">
	<p class="boxTitle">Sub Concepts</p>	
	<? foreach($subClasses as $subConcept){  ?>
	
				<p class="linkedOntologyConcept">
				
				<a onclick=" navigationConcept('<? echo $subConcept->getId();?>'); " ><? echo $subConcept->getName(); ?></a><br/>
				<!-- <span class="desc">Description of the linked ontology concept</span><br/> -->

				<!-- tags line -->
				<span class="tags">Tags:
				<? $conceptTags = $res->getAllTagsByConcept($subConcept);
				if(!empty($conceptTags))
				   foreach ($conceptTags as $tag=>$mapping){
						?>
						<a onclick="plugsAndConceptFromTag('<? echo $tag ?>');"><? echo $tag; ?></a>, 
					<? } ?>	
				</span><br/>
				
			</p>
	<?	} ?>
	</div>
	
	<? }  ?>	
		
	</div>