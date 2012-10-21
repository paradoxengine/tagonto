<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<!--  META -->
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="keywords" content="tagonto, tag, find, ontology, ontologies" />
	<meta name="description" content="An engine exploiting ontologies in finding tagged contents" />
<!--  STYLES -->
	<link href="../css/styleTagonto.css" rel="stylesheet" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/gwidgets.css" type="text/css" media="screen" title="gWidgets" charset="utf-8">
<!--  JAVASCRIPTS -->
<!--<script src="js/prototype.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/scriptaculous/scriptaculous.js?load=effects" type="text/javascript" charset="utf-8"></script>
	<script src="js/base.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/gwidgets.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/tagonto.js" type="text/javascript" charset="utf-8"></script> -->


<title>Tagonto - Help</title>
</head>

<body>
	<div id="pageWidth">
	
	<?php include("../includes/topInclude.php"); ?>
	
	<div id="infoArea">
		<h1>How to</h1>
		
		<p><h2>Basic functions: Web and Ontology search engine tag-based</h2>
		Inserting a keyword in the top right text field, a search for it will start both in the web, within five given tag-based systems, 
		and among the ontology concepts. Remember to press the button "GO!" after typing the word: an ENTER won't be considered.<br/>
		Web results will be shown in the upper part of the page, organized with respect to the web site they belong to. You can select the site 
		using the tabs on top, and then only results related to the selected site will be loaded, to make the contents available quicker.<br/>
		Every result is a link to its page in its site.<br/>
		Meanwhile, tagonto goes through the ontology searching for concepts tagged with that keyword. As soon as it found them, a <i>disambiguation box</i> 
		appears. It contains some concepts, among which the user can select the one she wants to map the tag on. The choosen 
		concept will be shown in a orange box, together with some information about it, such as tags mapped on it, its URI in the 
		ontology, its <i>instances</i>. <br/>Other boxes will appear too, each containing similar information and representing concepts related 
		to the selected one. The property through which they have been reached is displayed on the top. You can click over the name of each of 
		the related concepts to go through the Ontology,, or you can select one of the tag to repeat the main search, both in the ontology 
		and in the web. 
		</p>
		
		<p><h2>Personal Area</h2>
		Through the login area users can enter their personal area, where one more feature is available: the chance to map a tag on an arbitrary ontology 
		concept in the ontology, when none of the concepts retrieved by the engine are satisfying. <br/>
		To <b>register</b>, user has to type a user name and a password in the login box, and then press <i>Register</i> button. From then on, she 
		will be able to login using the same data.
		</p>
		
	</div>

	
	</div>
	<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body>
</html>