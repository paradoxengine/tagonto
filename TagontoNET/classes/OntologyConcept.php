<?php

class OntologyConcept{

	private $name = NULL;
	private $uri = NULL;
	
	//NOTE THAT ASSOCIATEDTAGS is an array with ['TAG']=>[['USERID']=>['SIGNIFICANCE LEVEL']]
	private $associatedTags = array();
	private $id = NULL;
	
	

//----------------------------------setters------------------------------------//
		
		public function setName($n){
		$this->name = $n;
	}
	
		public function setId($id) {
			$this->id = $id;
		}
	
		public function setAssociatedTags($t){
		$this->associatedTags = $t;
		}

		//TODO add some checking here for duplicate tags!
		public function addAssociatedTag($t){
			$this->associatedTags = array_merge($this->associatedTags,$t);
		}
		
		public function setUri($uri) {
			$this->uri = $uri;
		}
	
	
//---------------------------------getters--------------------------------------//	
	
	public function getName(){
		
		return $this->name;
	}
	
	
	public function getId() {
		return $this->id;
	}
	
	/**
	 * Will return an array of strings containing all the instances of a given concept
	 *
	 * @return array()
	 */
	public function getInstances() {
		if(empty($this->id))
			return array();
		
		$INSTANCEQUERY = "SELECT * FROM instances WHERE concept = '$this->id'";

		
		$instances = mysql_query($INSTANCEQUERY);
		while($inst = mysql_fetch_assoc($instances)) {
			$return[] = $inst['instanceUri'];
		}
		return $return;	
	}
	
	public function getAssociatedTags(){
		return $this->associatedTags;
	}
	
	public function getUri() {
		return $this->uri;
	}
}

?>