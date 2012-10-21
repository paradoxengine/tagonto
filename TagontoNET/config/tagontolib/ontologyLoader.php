<?php
	if($_POST['tagontoUrl'] && $_POST['uri']) {
		$ch = curl_init($_POST['tagontoUrl']);		
		curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
		curl_setopt($ch, CURLOPT_POST,1);
		$params = "uri=".$_POST['uri'];
		//TODO set variable userid!!!!!
		curl_setopt($ch, CURLOPT_POSTFIELDS,$params);
		$xmlanswer = curl_exec($ch);	

		 //	Getting the document on the right mood
 		header("Content-Type: application/xml");
		echo $xmlanswer;
		
	}
	else {
?>
<html>
<head>
	<link href="css/configStyle.css" rel="stylesheet" type="text/css" media="screen" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Tagonto - config</title>
</head>
	<body>
		<div id="pageWidth">
		<h1>Tagonto Configuration Manager</h1>
		<div id="description">This page lets you load any Ontology in TagontoLIB, setting tagonto end point and the URI of the Ontology to be load. The default one 
		is the wines' one.</div>
		<br/>
		<br/>
		<form method="post">
			<itemset>
				<input type="text" name="tagontoUrl" size="90" value="http://tagontohost:9000/ontologies/getbyuri"> Tagonto Ontology load endpoint</input>
				<p><i>Set here the location of the Tagonto endpoint where to load the Ontology</i></p>
				<input type="text" name="uri" size="120" value="http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine"> Ontology Uri</input><br/>
				<p><i>Set here the URI of the Ontology to load</i></p>
				<input type="submit" value="setOntology"/>
			</itemset>
		</form>
		</div>
		<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
	</body>
</html>
<? } ?>