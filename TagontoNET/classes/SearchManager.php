<?php

class SearchManager{
	
	/**
	* returns an array containing only all resource consider, without results
	*/
	function getResources(){
		return $resources;
	}
	
	function getResourceResults(){
	
	}
	
	function getResourceResultsByTag($tag){
		$TagontoNET = new TagontoNET();
		$resources = $TagontoNET->getResourcesForTag($tag);
		return $resources;
	}
		
}
?>