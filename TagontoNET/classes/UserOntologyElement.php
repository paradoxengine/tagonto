<?php

class UserOntologyElement extends OntologyElement{

	private $user = NULL;
	private $associatedUserTags = NULL;
	
	

	/*
	 *  Appends a tag to the list of "user" tags associated to the current ontologyElement
	 */
	public function addAssociatedUserTag($tag){
		if ($this->associatedUserTags == NULL) {
			$this->associatedUserTags = array($tag);
		} else
			$this->associatedUserTags[] = $tag;
	}
	

//----------------------------------setters------------------------------------//
		
	
	public function setAssociatedUserTags($a){
		$this->associatedUserTags = $a;
	}
	
	public function setUser($id){
		$this->user = $id;
	}
	
	
//---------------------------------getters--------------------------------------//	
	

	public function getAssociatedUserTags(){
		return $this->associatedUserTags;
	}

	public function getUser(){
		return $this->user;
	}

}

?>