<?php
	
require_once(dirname(__FILE__)."/OntologyConcept.php");


class Ontology{
	
	
	 
	//ID of this ontology. HAS TO BE SET!!!
	 private $_ID;
	 private $_concept_table;
	 private $_properties_table;
	 private $_declared_properties_table;
	 private $_domain_range_table;
	 private $conceptsURItoID;
	 private $conceptsIDtoURI;
	 private $propertyIDtoURI;
	 private $propertyURItoID;
	 

	 
	 function __construct($ID, $concept_table = 'ontology_concepts',
	 						$declared_properties_table= 'ontology_declared_properties',
	 						$properties_table = 'ontology_properties',
	 						$domainrange = 'domain_range') {
	 	if(empty($ID)) return null;
	 		else $this->_ID = $ID;
	 	$this->_concept_table = $concept_table;
	 	$this->_declared_properties_table = $declared_properties_table;
	 	$this->_properties_table = $properties_table;
	 	$this->_domain_range_table = $domainrange;
	 	$CONCEPTCACHINGQUERY = "SELECT * FROM $concept_table WHERE ontology=$ID";
	 	$res = mysql_query($CONCEPTCACHINGQUERY);
	 	
	 	//Populating concepts
	 	while($conc = mysql_fetch_assoc($res)) {
	 		$this->conceptsIDtoURI[$conc['id']] = $conc['name'];
	 		$this->conceptsURItoID[$conc['name']] = $conc['id'];
	 	}
	 	
	 	$PROPERTYCACHINQUERY = "SELECT * FROM $declared_properties_table WHERE ontology=$ID";
	 	$res = mysql_query($PROPERTYCACHINQUERY);
	 	
	 	//Populating properties
	 	while($prop = mysql_fetch_assoc($res)) {
	 		$this->propertyIDtoURI[$prop['id']] = $prop['name'];
	 		$this->propertyURItoID[$prop['name']] = $prop['id'];
	 	}
	 	
	 }
	 
	/**
	 * getRelatedOntologyElements will fetch all the related concepts from the ontology
	 * @param $Concept OntologyConcept
	 * @return array Concept[]
	 */ 
	function getRelatedOntologyConceptsAndInstances($OntologyConcept = '') {
		$conceptid = $OntologyConcept->getId();
		if(!is_numeric($conceptid) || empty($conceptid))
			return false;
		
		//Get properties on both sides, but only if we've got an instance of the related concept
	$getRelatedQuery = "SELECT DISTINCT DR.subject as Subject, DR.property as Property , DR.object as Object, RI.instanceUri as URI 
		FROM domain_range AS DR LEFT JOIN reachable_instances AS RI  ON DR.subject = RI.subject 
		JOIN instances ON instances.concept = DR.object 
		WHERE instances.instanceUri = RI.instanceUri AND (DR.subject = $conceptid OR object = $conceptid)

		 UNION 

    	SELECT DISTINCT DR.subject as Subject, DR.property as Property, DR.object as Object, RI.instanceUri as URI
		FROM domain_range AS DR LEFT JOIN reachable_instances AS RI  ON DR.object = RI.subject 
		JOIN instances ON instances.concept = DR.object 
		WHERE instances.instanceUri = RI.instanceUri AND (DR.subject = $conceptid OR object = $conceptid)";
		
	
		$result = mysql_query($getRelatedQuery);
		
		//We are now creating a data structure with concept/properties touple and their associated instances
		//that is, instances of a given class in relation with our source concept due to the specified property
		while($assoc = mysql_fetch_assoc($result)) {
				if($conceptid == $assoc['Subject']) {
					$ontconcept = $this->getConceptByID($assoc['Object']);
					 }
				elseif($conceptid == $assoc['Object']) {
					$ontconcept = $this->getConceptByID($assoc['Subject']);
				} 
					$propertyURI = $this->propertyIDtoURI[$assoc['Property']];
					$myprop = array();
					$myprop['concept'] = $ontconcept;
					$myprop['property'] = $propertyURI;
					
					//If we already have instances for this property-concept pair, fetch them, otherwise create a new array
					if(empty($associatedConcepts[$ontconcept->getId(). '_' . $assoc['Property']]['instances']))
						$instances = array();
					else 
						$instances = $associatedConcepts[$ontconcept->getId(). '_' . $assoc['Property']]['instances'];
					$instances[] = $assoc['URI'];
					
					$myprop['instances'] = $instances;					
					$associatedConcepts[$ontconcept->getId(). '_' . $assoc['Property']] = $myprop;
		}

		//We shall now merge this result with another query to extract all the concept without an instance
		$getRelatedQuery = "select * from $this->_domain_range_table where subject = $conceptid OR object = $conceptid";
		
		$result = mysql_query($getRelatedQuery);
		
		while($assoc = mysql_fetch_assoc($result)) {
				if($conceptid == $assoc['subject']) {
					$ontconcept = $this->getConceptByID($assoc['object']);
				}
				elseif($conceptid == $assoc['object']) {
					$ontconcept = $this->getConceptByID($assoc['subject']);
				} 
					$propertyURI = $this->propertyIDtoURI[$assoc['property']];
					$myprop = array();
					$myprop['concept'] = $ontconcept;
					$myprop['property'] = $propertyURI;
					if(empty($associatedConcepts[$ontconcept->getId(). '_' . $assoc['property']]['instances']))
						$myprop['instances'] = array();
					else 
						$myprop['instances']  = $associatedConcepts[$ontconcept->getId(). '_' . $assoc['property']]['instances'];
					$instances[] = $assoc['URI'];
					$associatedConcepts[$ontconcept->getId(). '_' . $assoc['property']] = $myprop;
			} 

		return $associatedConcepts;
	}
	
	
	/**
	 * getSuperAndSubclasses will fetch all the super and sub classes from the ontology
	 * @param $Concept OntologyConcept
	 * @return array Concept[]
	 */ 
	function getSuperAndSubclasses($OntologyConcept = '') {
		$conceptid = $OntologyConcept->getId();
		if(!is_numeric($conceptid) || empty($conceptid))
			return false;
		
		//Get properties on both sides
		$getRelatedQuery = "SELECT * FROM subclassof WHERE subclass = $conceptid OR superclass = $conceptid";
		
	
		$result = mysql_query($getRelatedQuery);
		while($assoc = mysql_fetch_assoc($result)) {
				if($conceptid == $assoc['subclass']) {
					$ontconcept = $this->getConceptByID($assoc['superclass']);
					$superClasses[] = $ontconcept;
				}
				elseif($conceptid == $assoc['superclass']) {
					$ontconcept = $this->getConceptByID($assoc['subclass']);
					$subClasses[] = $ontconcept;
				} 
		}
		$return['subclasses'] = $subClasses;
		$return['superclasses'] = $superClasses;
		return $return;
	}
	
	
	
	
	
	
	/**
	 * Will return a conceptID given its URI, checking the cache or making queries
	 * when needed
	 *
	 * @param String $URI
	 * @return int $ID
	 */
	function getConceptIDFromURI($URI) {
		if(empty($URI)) 
		   return 0;
		else {
			//Let's check if we've got contents in cache (very likely)
			if(empty($this->conceptsURItoID[$URI])) {
				//We don't let's try on the database
				$SAFEURI = mysql_real_escape_string($URI);
				$GETIDFROMURIQUERY = "SELECT id FROM $this->_concept_table WHERE name ='$SAFEURI' AND ontology = '$this->_ID'";
				$res = mysql_query($GETIDFROMURIQUERY);
				$res = mysql_fetch_assoc($res);
				return $res['id'];
			}
			else {
				return $this->conceptsURItoID[$URI];	
			}
		}
	}
	
	
	/**
	 * Will return a complete OntologyConcept given its internal id
	 *
	 * @param int $ConceptId
	 */
	function getConceptByID($ConceptId) {
		if(!is_numeric($ConceptId))
			return null;
		
		$GETCONCEPTFROMIDQUERY = "SELECT * FROM $this->_concept_table WHERE id ='$ConceptId' AND ontology = '$this->_ID'";

		$res = mysql_query($GETCONCEPTFROMIDQUERY);

		//Since it's a primary key, we expect only one result
		if($res = mysql_fetch_assoc($res)) {
			$OntConcept = new OntologyConcept();
			//TODO extract the name
			$OntConcept->setName(str_replace('#','',strrchr ($res['name'], '#')));
			$OntConcept->setId($res['id']);
			$OntConcept->setUri($res['name']);		
			return $OntConcept;
		}
		
		return null;
	}
	
	
	/**
	* will return every concept in the ontology
	***/
	function getAllConcepts() {
		foreach($this->conceptsURItoID as $uri=>$id) {
		
			$concept = new OntologyConcept();
			$concept->setId($id);
			$concept->setUri($uri);
			//removing the # and getting the concept name
			$concept->setName(str_replace('#','',strrchr ($uri, '#')));
			$concepts[] = $concept;
		}
		return $concepts;
	}
}
	
?>