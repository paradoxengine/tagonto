<?php


 require_once dirname(__FILE__).'/OntologyConcept.php';
 require_once dirname(__FILE__).'/Ontology.php';

class ReasoningManager{
	
	
	
	//URL to request for mapping
	private $_mapping_endpoint;
	
	function __construct($mapping_endpoint) {
		$this->_mapping_endpoint = $mapping_endpoint;
	}
	
	public function getConceptsByTag($OntologyID = '1',$Tag,$userId = '1'){
		if(empty($Tag)) return null;
		$TagLabel = $Tag->getLabel();
		
		//Going curl!
		$url = $this->_mapping_endpoint . $OntologyID . '/mapping'; 	
		$ch = curl_init($url);		
		curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
		curl_setopt($ch, CURLOPT_POST,1);

		$params = "tag=$TagLabel&userId=$userId&doGreedyMatch=false";
		//TODO set variable userid!!!!!
		curl_setopt($ch, CURLOPT_POSTFIELDS,$params);
		$xmlanswer = curl_exec($ch);
		curl_close ($ch);


		//No answer from tagonto
		if(empty($xmlanswer)) {
			return array();
		}

		//Answer gotten
		//TODO error handling!
		$doc = new DOMDocument();
		$doc->preserveWhiteSpace = false;
		//Loading the XML doc
		$doc->LoadXML($xmlanswer);
		$xpath = new DOMXPath($doc);
		//Extracting tag information
		$query = '//mapping';
		
		//TODO error handling here too
		$mappings = $xpath->query($query);
		
		//Setupping ontologyconcepts
		foreach($mappings as $mapping) {
			$concept = new OntologyConcept();
			//Setting name and uri
			$concept->setName($mapping->getAttribute('concept'));
			$concept->setUri($mapping->getAttribute('conceptUri'));
		
			echo $CONCEPTS_TABLE;
			//Here we get the element ID
			$ONT = new Ontology($OntologyID);
			$concept->setId($ONT->getConceptIDFromURI($mapping->getAttribute('conceptUri')));
			//Preparing an array that will be merged to a given element mapping
			//TODO caching here?!
			$maparray[1] = 	$mapping->getAttribute('significance');
			$tagarray[$mapping->getAttribute('tag')] = $maparray;
			$concept->addAssociatedTag($tagarray);
			$Concepts[] = $concept;
		}
		return $Concepts;
	}

	
	public function mapTagOnConcept($tag, $concept,$userid,$significance,$ontologyid){
		if(empty($tag) || empty($concept) || empty($significance) || empty($ontologyid)) {
			return false;
		}
			$url = $this->_mapping_endpoint . $ontologyid . '/force_mapping';
			$taglabel = $tag->getLabel();
			$uri = $concept->getUri();

			$params = "userId=$userid&tag=$taglabel&conceptUri=$uri&significance=$significance";
			
			//Going curl
			$ch = curl_init($url);		
			curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
			curl_setopt($ch, CURLOPT_POST,1);
			curl_setopt($ch, CURLOPT_POSTFIELDS,$params);

			$xmlanswer = curl_exec($ch);
			curl_close ($ch);
			
			//Parsing the answer
			$doc = new DOMDocument();
			$doc->LoadXML($xmlanswer);
			$xpath = new DOMXPath($doc);
			//	Extracting tag information
			$query = '/tagonto';
			$requeststatus = $xpath->query($query);
			//No result here
			if($requeststatus->length == 0)
				return false;
			//Checking requestSatisfied
			$requestresult = $requeststatus->item(0)->getAttribute('requestSatisfied');
			if($requestresult == 'true')
				return true;
			else
				return false;
			
	}
	
	/**
	* Will query the database fetching all the known mappings for the given concept
	* @return ['TAG']=>[['USERID']=>['SIGNIFICANCE LEVEL']]
	***/
	public function getAllTagsByConcept($OntologyConcept){
		$id = $OntologyConcept->getId();
		if(empty($id) || !is_numeric($id))
			return array();
		
		//TODO parametryze me
		$minsign = '0.1';
		
		$MappingSelectionQuery = "SELECT * FROM mappings WHERE concept=$id AND significance > $minsign";
		$mappings = mysql_query($MappingSelectionQuery);
		
		//we are creating an array of mappings associated to every tag
		while($map = mysql_fetch_assoc($mappings)) {
			$tag = new Tag($map['tag']);	
			$mapping[$map['user']] = $map['significance'];
			$Tags[$map['tag']] = $mapping;
		}
		return $Tags;
	}

	
}

?>