<?php

class Resource{
	
	private $title = NULL;
	
	//file url
	private $logo = NULL;
	
	private $description = NULL;
	
	private $homeUrl = NULL;
	
	//array containg objects of type result
	private $results = NULL;
	
	
	//----------------------------getters-----------------------------//
	public function getTitle(){
		return $this->title;
	}
	
	public function getLogo(){
		return $this->logo;
	}
	
	public function getDescription(){
		return $this->description;
	}
	
	public function getHomeUrl(){
		return $this->homeUrl;
	}
	
	public function getResults(){
		return $this->results;
	}
	
	//---------------------------setters-------------------------------//
	
	public function setTitle($t){
		$this->title = $t;
	}
	
	public function setLogo($l){
		$this->logo = $l;
	}
	
	public function setDescription($d= ""){
		$this->description = $d;
	}
	
	public function setHomeUrl($u= ""){
		$this->homeUrl = $u;
	}
	
	public function setResults($r= ""){
		$this->results = $r;
	}
	
}
?>