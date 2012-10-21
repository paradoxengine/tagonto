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
<title>Tagonto - About</title>
</head>

<body>
	<div id="pageWidth">
	<?php include("../includes/topInclude.php"); ?>
	<div id="infoArea">
		<h1>Tagonto</h1>
		<p><h2>What is</h2>
			Tagonto is a search engine tag- and ontology-based. This short definition highlights the two different aspects which characterize 
			Tagonto: the tag-based search engine and the support for ontology navigation. But what's most, it highlight the fusion of these 
			aspects, which is the main feature of Tagonto. Tagonto allows to <i>map</i> tags on ontology concepts, and on the other side, to start 
			from ontology concepts, to view tag mapped on them, and to search the web for contents tagged with that tag. <br/>
			Tagonto allows to map tags on ontology concepts.
		</p>
		
		<p><h3>Tag-based search engine</h3>
			As a search engine, Tagonto supplies contents tagged with the searched keyword within five popular tag-based systems: 
			<i>Delicious</i>, <i>Flickr</i>, <i>43Things</i>, <i>YouTube</i>, <i>Zvents</i>.
		</p>
		
		<p><h3>Ontologies surfing</h3>
			Tagonto is able to load an arbitrary ontology. In practice, its present implementation is based on a given ontology,  
			wines one (that you can find at <a href="http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine">http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine</a>). 
			From a user experience point of view, Tagonto's interface allows to view each ontology concept together with tags mapped on it and other ontology concepts related 
			to it.
		</p>		
	</div>

	
	</div>
	<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body>
</html>