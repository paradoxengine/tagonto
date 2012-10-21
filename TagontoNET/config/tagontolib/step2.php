<?php

foreach($_POST as $key=>$value) {
	$TagontoLibConfiguration->$key = $value;
}

	$TagontoLibConfiguration->save();
?>

<html>
<head>
	<link href="config.css" rel="stylesheet" type="text/css" media="screen" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Tagonto - config</title>
</head>
	<body>
		<div id="pageWidth">
			<h1>Tagonto Configuration Manager</h1>
			<br/>
			<p class="confCreated">Configuration file has been correclty created.</p>
			<p class="confCreated">Click <a href="TagontoConfiguration.php">here</a> to view it.</p> 
		</div>
			<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
	</body>
</html>