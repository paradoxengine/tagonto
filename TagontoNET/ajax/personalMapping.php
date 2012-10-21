<?php
	//include
	require_once('../config.php');
	
	$ontology = new Ontology($ONTOLOGYID);
	$tagToMap = $_GET['tag'];
	
	//TODO here a function to get all concepts in the ontology
	 $ontologyConcepts = $ontology->getAllConcepts(); 
	
	//counter used to set the number of columns in the table
	$i = 0;
	
	//$ontologyConcepts = array("uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove", "dieci", "undici", "dodici", "tredici");

?>

<div id="wholeOntology">
<table>
	<tr>
	<? foreach ($ontologyConcepts as $concept){
	?>
		<td><a onclick="choosedConcept('<? echo $concept->getId();?>','<? echo $tagToMap ?>');"><? echo $concept->getName() ?></a></td> 
		
		<? $i++; 
		if (($i % 4) == 0) { ?>
			</tr><tr>
		<? } 
	}
	?>
	</tr>
</table>
</div>