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
		<h1>Istruzioni per l'uso</h1>
		<p><h2>Funzionalit&agrave; di base: ricerca nel web e nell'ontologia</h2>
			La funzionalit&agrave; base di Tagonto &egrave; la ricerca per tag. Inserendo un termine nel campo di testo in alto a destra 
			si potranno visualizzare i contenuti "taggati" con quel termine all'interno di cinque diversi siti basati su sistemi a 
			tag. I risultati verranno visualizzati divisi per sito, e ogni volta saranno caricati solo quelli appartenenti al sito 
			selezionato. Questo allo scopo di velocizzare la presentazione dei risultati, dal momento che l'operazione &egrave; piuttosto 
			lenta.I risultati della ricerca saranno mostrati nella parte superiore della pagina.
		</p>
		<p><h2>Navigazione nell'ontologia</h2>
			Contemporaneamente alla ricerca su web, il motore effettua una ricerca anche all'interno dell'ontologia. Nella met&agrave; inferiore della pagina comparir&agrave;
			un riquadro per la <i>disambiguazione</i>: dal momento che &egrave; possibile (oltre che piuttosto probabile) che 
			diversi concetti dell'ontologia siano "taggati" con lo stesso tag, questo riquadro consente di scegliere tra i concetti 
			individuati dal motore quello che pi&ugrave; si adegua a quanto cercato. Una volta effettuata la disambiguazione, verr&agrave; mostrato 
			il concetto scelto assieme ai tag ad esso associato e agli altri concetto dell'ontologia a cui &egrave; collegato da qualche relazione.<br/>
			A questo punto &egrave; possibile navigare attraverso l'ontologia "saltando" di concetto in concetto, oppure reiterare la ricerca 
			nel web per i tag collegati ai diversi concetti.
		</p>
		<p><h2>Area personale</h2>
			L'area di login d&agrave; accesso ad una ulteriore funzionalit&agrave;: la possiblit&agrave; di effettuare un mapping arbitrario 
			di un tag su uno qualunque dei concetti dell'ontologia, nel caso in cui nessuno dei mapping esistenti risulti 
			soddisfacente.<br/>
			Si &egrave; scelto di associare questo mapping al singolo utente per mantenere la dimensione soggettiva della scelta.<br/>
			Per <b>registrarsi</b> &egrave; sufficiente inserire un nome utente e una password e clickare sul pulsante <i>Register</i>.<br/>
			Per <b>effettuare l'accesso</b> bisogna inserire un nome utente e una password registrati.<br/>
		</p>
		
	</div>

	
	</div>
	<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body>
</html>