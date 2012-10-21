<?php
class Tag{
	
	private $label = NULL;
	
	//maybe to be used in the future
	private $synonims = NULL;
	private $context = NULL;
	
	function __construct($l){
		$this->label = $l;
	}
	
	public function setLabel($l){
		$this->label = $l;
	}
	
	public function getLabel(){
		return $this->label;
	}
	
	
	public function getContext() {
		return $this->context;
	}
	
	public function setContext($c) {
		$this->context = $c;
	}
	
}
?>