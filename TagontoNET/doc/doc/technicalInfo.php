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
		<h1>L'applicazione: descrizione e scelte implementative</h1>
		<p><h2>L'interfaccia web</h2>
		Innanzitutto, la scelta di fare un'interfaccia web &egrave; stata dettata dal desiderio di rendere l'applicazione facilmente 
		accessibile e per una questione di coerenza rispetto ai suoi contenuti.<br/>
		La divisione in due parti rende epliciti i due aspetti caratterizzanti l'applicazione: quello della ricerca nel web e quello 
		della ricerca all'interno dell'ontologia.<br/>
		</p>
		
		<p><h3>I risultati dal web</h3>
		I risultati della ricerca sul web sono suddivisi in base al sito di provenienza, e sono stati limitati in numero perch&eacute; 
		sarebbero stati altrimenti troppo numerosi e ci&ograve; avrebbe reso la pagina poco leggibile.<br/>
		Per i risultati testuali viene visualizzato il titolo e quando disponibile una breve descrizione. Per Flickr e YouTube viene 
		visualizzata invece una anteprima delle immagini (o dei video, nel secondo caso). In tutti i casi i risultati presentano link 
		navigabili che conducono al contenuto che rappresentano.
		</p>
		
		<p><h3>Scelte implementative</h3>
		L'interfaccia &egrave; stata sviluppata con il supporto di AJAX. Questo per sopperire alla lantezza che caratterizza il motore: 
		le attivit&agrave; svolte da Tagonto, in modo particolare quelle di ricerca, sono piuttosto onerose per il sistema, e questo si 
		traduce in un forte ritardo nella visualizzazione dei risultati. AJAX ha permesso, tra le altre cose, di limitare questo ritardo 
		facendo s&igrave; che i risultati relativi ad ogni singolo sito web fossero caricati solo al momento della sua selezione, anzich&egrave;
		tutti contemporaneamente.<br/>
		Un altro effetto della scelta di AJAX &egrave; il fatto che le diverse azioni dell'utente non comportano il ricaricarsi 
		di tutta la pagina ma solo della porzione interessata.<br/>
		Oltre agli script java, necessari per l'utilizzo di alcune librerie AJAX, le parti dinamiche dell'interfaccia sono state sviluppate 
		utilizzando PHP5, e sfruttandone il supporto per la programmazione ad oggetti.<br/>
		Per quanto riguarda i risultati della ricerca nel web, essi sono forniti nella forma di file xml, e trasformati in html attraverso un 
		foglio di stile XSL.
		</p>
		
	</div>

	
	</div>
	<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body>
</html>