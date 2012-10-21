/*  TAGONTO JS
 * 
 *  Javascript library for tagonto
/*--------------------------------------------------------------------------*/

function disambiguatorCaller() {
	 var tag = document.getElementById("ontology_submit").getValue();
	 new Ajax.Updater('disambiOntologyContainer', 'ajax/disambiguator.php?tag='+tag); 
	}
	
function doLogin() {
	 var user = document.getElementById("usrfield").getValue();
	 var pass = document.getElementById("pwdfield").getValue();
	 new Ajax.Updater('loginArea', 'ajax/doLogin.php?user='+user+'&pass='+pass); 
	}

function doLogout() {
	 new Ajax.Updater('loginArea', 'ajax/doLogin.php?logout=true'); 
	}
		

function doRegister() {
	 var user = document.getElementById("usrfield").getValue();
	 var pass = document.getElementById("pwdfield").getValue();
	 new Ajax.Updater('loginArea', 'ajax/doLogin.php?register=true&user='+user+'&pass='+pass); 
	}
		
	
function choosedConcept(concept,tag){
		new Ajax.Updater('disambiOntologyContainer', 'ajax/navigator.php?concept='+concept+'&tag='+tag);
	}
	
function navigationConcept(concept){
		new Ajax.Updater('disambiOntologyContainer', 'ajax/navigator.php?concept='+concept+'&noTag=true');
	}
	
function plugsAndConcept(){
		var tag = document.getElementById("tagField").getValue();
		new Ajax.Updater('disambiOntologyContainer', 'ajax/disambiguator.php?tag='+tag, {
	 onCreate: function(){
	 	Element.hide('ontologyDescription');
	 }
	 });
		new Ajax.Updater('tabtab', 'ajax/pluginTabs.php?tag='+tag, {onComplete: function() {
		 new gTab('tabtab');
		}});
		
	}
	
	
	function plugsAndConceptFromTag(tag){
		new Ajax.Updater('disambiOntologyContainer', 'ajax/disambiguator.php?tag='+tag, {
	 onCreate: function(){
	 	Element.hide('ontologyDescription');
	 }
	 });
		new Ajax.Updater('tabtab', 'ajax/pluginTabs.php?tag='+tag, {onComplete: function() {
		 new gTab('tabtab');
		}});
		
	}
	

	
/**	
 * shows in ontology area a box containing all concepts in ontology
 */
function showAllConcepts(tag){
		new Ajax.Updater('disambiOntologyContainer', 'ajax/personalMapping.php?tag='+tag);
	}
	
function registerIndicator(){
	Ajax.Responders.register({
	    onCreate: function(){ Element.show('loadingImg'); Element.show('loadingImgOntology')}, 
	    onComplete: function(){Element.hide('loadingImg'); Element.hide('loadingImgOntology')}
	    });
	}



Event.observe(window,'load',registerIndicator,false);