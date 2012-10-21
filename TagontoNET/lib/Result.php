<?php
class Result{

	private $resource = NULL;
	
	/*
	* int values
	* 1 - img
	* 2 - text
	*/
	private $contentType = NULL;
	private $title = NULL;
	private $description = NULL;
	private $url = NULL;
	
	/*
	* piece of content result to be shown, image or a row of text
	*/
	private $shownContent = NULL;
	private $tagSearched = NULL;
	
	//------------------------------------getters------------------------------------//
	public function getResource(){
		return $this->resource;
	}
	
	public function getContentType(){
		return $this->contentType;
	}
	
	public function getTitle(){
		return $this->title;
	}
	
	public function getDescription(){
		return $this->description;
	}
	
	public function getUrl(){
		return $this->url;
	}
	
	public function getShownContent(){
		return $this->shownContent;
	}
	
	public function getTagSearched(){
		return $this->tagSearched;
	}
	
	
	//------------------------------------setters--------------------------------------//
	public function setResource($r){
		$this->resource = $r;
	}
	
	public function setContentType($c){
			$this->contentType = $c;
	}
	
	public function setTitle($t){
		$this->title = $t;
	}
	
	public function setDescription($d){
		$this->description = $d;
	}
	
	public function setUrl($u){
		$this->url = $u;
	}
	
	public function setShownContent($s){
		$this->shownContent = $s;
	}
	
	public function setTagSearched($t){
		$this->tagSearched = $t;
	}

}
?>