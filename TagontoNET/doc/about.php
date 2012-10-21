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
		<p><h2>Cos'&egrave;</h2>
		Tagonto &egrave; un motore di ricerca a tag basato su ontologie. Questa definizione, pur sommaria e imprecisa, mette in evidenza
		i due aspetti distinti che lo caratterizzano: l'essere un motore di ricerca tag-based, ed il fornire un supporto per la navigazione 
		attraverso ontologie. La sua caratteristica pi&ugrave; importante &egrave; l'unione di questi due aspetti. Tagonto consente infatti di mappare tag 
		sui concetti dell'ontologia, e (nella "direzione" opposta) consente di partire dai tag collegati ai diversi concetti 
		per effettuare ricerche nel web. Prima di entrare nei dettagli delle due funzionalit&agrave; singole, vediamo meglio cosa significa
		effettuare una ricerca basata sui tag e su una ontologia.<br/>
		Una <i>ontologia</i> &egrave;, banalizzando, una serie di <i>concetti</i> collegati tra loro da relazioni di varia natura. Le relazioni ci 
		permettono di navigare tra i concetti. <br/>
		Un <i>tag</i> &egrave; invece una "etichetta" associata ad un data informazione o a parte di essa, includendo in "informazione" diversi 
		tipi di dati, quali immagini, video, articoli in siti web,... Il tag può essere associato ad una informazione indifferentemente 
		dal suo creatore o da chiunque altro. <br/>
		L'uso dei tag si &egrave; molto diffuso nel contesto del Web2.0. Esistono all'interno di internet molti siti che mettono a disposizione 
		sistemi a tag, ovvero consentono ai loro utenti di associare etichette a immagini o articoli che li interessano, e di effettuare 
		poi ricerche all'interno dei contenuti del sito basate sui tag che via via sono stati associati a tali contenuti dai diversi 
		utenti. Una ricerca di questo genere non si basa quindi su criteri "oggettivi" ma ha una base "sociale", dal momento che i 
		suoi risultati dipendono dai tag che altre persone hanno associato ai diversi contenuti.<br/>
		L'unione in tagonto di ontologie consente di associare i tag oltre che a "contenuti" distributi in internet ai "concetti" dell'ontologia. 
		In questo modo, navigando all'interno di un'ontologia, &egrave; possibile esaminare i tag associati a ciascun concetto e utilizzarli quindi 
		per trovare nel web i contenuti etichettati con quel tag, e dunque associati a quel concetto.<br/>
		Vediamo ora come funziona ciscuno dei due aspetti di tagonto.
		</p>
		
		<p><h3>Motore di ricerca tag-based</h3>
		Come motore di ricerca, Tagonto sfrutta le informazioni fornite dai sistemi a tag di alcuni tra i siti pi&ugrave; noti 
		(<i>43Things</i>, <i>Delicious</i>, <i>Flickr</i>, <i>YouTube</i>, <i>Zvents</i>) per effettuare ricerche tag-based tra i loro contenuti.
		</p>
		
		<p><h3>Navigazione di Ontologie</h3>
		Per com'&egrave; stato sviluppato, Tagonto consente di importare un'ontologia arbitraria. Nella pratica la sua attuale implementazione 
		si basa su una ontologia in particolare, l'ontologia dei vini (<a href="http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine">http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine</a>). 
		Da punto di vista dell'interazione con l'utente, l'interfaccia consente di visualizzare assieme a ciascun concetto sia i tag ad esso associati 
		che i concetti dell'ontologia con cui &egrave; direttamente in relazione.
		</p>
		
		<h2>Fondamenti Teorici</h2>
		<h3>L'ontologia</h3>
		
	</div>

	
	</div>
	<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body>
</html>