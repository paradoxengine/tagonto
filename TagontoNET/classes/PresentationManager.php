<?php 

require_once ("SearchManager.php");
require_once ("lib/Tag.php");

class PresentationManager {

	private $tagLabel = NULL;
	private $tagAndSynLabel = NULL;
	
	/*
	* adds a new inserted synonim to the array containing the starting tag
	*/
	function addSynonimToTag($t){
		if ($tagLabel != NULL){
			if ($tagAndSynLabel == NULL) {
				$tagAndSynLabel = array($tag, $t);
			} else if ($tagAndSynLabel != NULL){
				$tagAndSynLabel[] = $t;
			}
		}
		
		$this->searchTagAndSyn($tagAndSynLabel);
	}
	
	//only returns the param variable
	function displaySearchedTag($tag){
		return $this->tag = $tag;
	}
	
	/*
	 * returns the results for the the searched tag,
	 * organized in Resources 
	 */
	function searchTag($tagLabel){
		$searcher = new SearchManager();
		//TODO controllare che il tag con quella etichetta non esista gi�!!!
		$tagToSearch = new Tag($tagLabel);
		
		
		// DA COMMENTARE PER TEST !!!!!!!!!!!
		/*
		if ($searcher->getResourceResultsByTag($tagToSearch) == NULL){
			return 2;
		}  else  $this->composeResources($searcher->getResourceResultsByTag($tagToSearch));
		*/
		return $searcher->getResourceResultsByTag($tagToSearch);
	}
	
	function searchTagAndSyn($tagAndSynArray){
		//TODO
		
	}
		
	function composeResults(){
		//TODO not used at the moment, corrispondent code integrated inside composeResultsInResources
	}
	
	/*
	* given a resource's results array packs them to
	* show them into a list
	*/	
	function composeResultsInResources($results){
		
		foreach ($results as $i){
			print "<p class='results'>";
			print "<bold>".$i->getTitle()."</bold>";
			print $i->getUrl();
			print "<br/>";
			print  "<i>".$i->getDescription()."</i>";
			print "<br/>";
			//controllare questo controllo! !!!!!!!!!!!
			if ($i->getContentType() == 1 && (strcmp(strstr($i->getContentType(), "."), ".jpg") == 0)) {
				print "<img class='resultImg' src='".$i->getShownContent()."'; />";
			} else {
				print $i->getShownContent();
			}
			print "</p>";
		}
	}
	
	function composeResources($resourceList){
		
		//CODICE DI PROVAA!!!!
		$risorsa1 = new Resource();
		$risorsa1->setLogo("HiernateIco.jpg");
		$risorsa1->setTitle("Titolo 1");
		$risorsa1->setDescription("descrizione della risorsa 1, tipo etc");
		

		$resourceArray = array($risorsa1);
			
		print "<div id='resourceBlock'>";
		foreach($resourceArray as $i){
			if ($risorsa1->getLogo() != NULL){
				print "<p><h2><a href='".$i->getHomeUrl()."'><img src='".$i->getLogo()."' /></a>".$i->getTitle()."</h2></p>";
			} else 
				print "<p><h2>".$i->getTitle()."</h2></p>";
			print "<p class='results'>".$i->getDescription()."</p>";
			print "<div>".$this->composeResultsInResources($i->getResults())."</div>";
			print "<br/>";
		}
		
		print "<p>That's not what you expected? Improve your search, insert a synonim: <input type='text' action='resultShower.php' method='POST' /></p>";
		
		print '<hr/></div>';
	}
	
}
?>