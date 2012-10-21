<?php
//The tagonto LIB configuration element
//$TagontoLibConfiguration;
?>
<html>
<head>
<link rel="stylesheet" href="config.css" type="text/css" media="screen" title="config" charset="utf-8">
</head>
<body>
<div id="pageWidth">
		<h1>Tagonto Configuration Manager</h1>
<div id="desc">TagontoLIB configuration: please note that you HAVE to provide correct setup for the <strong>bold</strong> labels.</div>


<div name="configdiv">
<form method="POST" action ="index.php?itemtoconfigure=tagontolib&step=2">
<fieldset>
<legend>TagontoLIB - Step 1: Paths</legend>

	<label class="first" for="RDF_CACHER_TAG_ONTOLOGY_URI">
	<strong>Cacher Tag Ontology URI</strong>
	</label> <input type="text" name="RDF_CACHER_TAG_ONTOLOGY_URI" size="50" 
	value="<? echo $TagontoLibConfiguration->RDF_CACHER_TAG_ONTOLOGY_URI; ?>"/>
</br>
	<label for="WORDNET_JWNL_CONFIG_FILE">
	<strong>Wordnet config file path</strong>
	</label> <input type="text" name="WORDNET_JWNL_CONFIG_FILE" size="50"
	value="<? echo $TagontoLibConfiguration->WORDNET_JWNL_CONFIG_FILE; ?>"/><br />
</br>
  <p>Please note that the paths here provided are needed and must be carefully chacked.</p>
  <p>As for the database, you have to create the Main Tagonto Database using the 
  <a href="tagontolib/tagonto.sql"> included SQL DDL file </a>. You can easily import it
  using phpmyadmin or with <em>mysql -u <strong>yourusername</strong> -p <strong>yourpassword</strong>
  databasename < tagonto.sql</em>.</p>
  <p>After the creation, you have to make sure that the tagonto user has full rights on the database</p>
  <p>Just create the Cacher DB, you are not required to create any table</p>



</fieldset>




<fieldset>
<legend>TagontoLIB - Step 2: Database Data</legend>

	<label class="first" for="RDF_CACHER_DB_TYPE">
	Cacher Database type
	</label> <input type="text" name="RDF_CACHER_DB_TYPE" size="50"
	value="<? echo $TagontoLibConfiguration->RDF_CACHER_DB_TYPE; ?>"/><br />
</br>
	<label for="RDF_CACHER_JDBC_URL">
	<strong>Cacher JDBC Url</strong>
	</label> <input type="text" name="RDF_CACHER_JDBC_URL" size="50"
	value="<? echo $TagontoLibConfiguration->RDF_CACHER_JDBC_URL; ?>"/><br />
</br>
	<label for=RDF_CACHER_DB_USER>
	<strong>Cacher Database User</strong>
	</label> <input type="text" name="RDF_CACHER_DB_USER" size="50"
	value="<? echo $TagontoLibConfiguration->RDF_CACHER_DB_USER; ?>"/><br />
</br>
	<label for=RDF_CACHER_DB_PASS>
	<strong>Cacher Database Password</strong>
	</label> <input type="text" name="RDF_CACHER_DB_PASS" size="50"
	value="<? echo $TagontoLibConfiguration->RDF_CACHER_DB_PASS; ?>"/><br />
</br>
	<label for=DATABASE_SERVER>
	<strong>Main Tagonto Database Server</strong>
	</label> <input type="text" name="DATABASE_SERVER" size="50"
	value="<? echo $TagontoLibConfiguration->DATABASE_SERVER; ?>"/><br />
</br>
	<label for=DATABASE_SCHEMA>
	Main Tagonto Database name
	</label> <input type="text" name="DATABASE_SCHEMA" size="50"
	value="<? echo $TagontoLibConfiguration->DATABASE_SCHEMA; ?>"/><br />
</br>
	<label for="DATABASE_USER">
	<strong>Main Tagonto Database Username</strong>
	</label> <input type="text" name="DATABASE_USER" size="50"
	value="<? echo $TagontoLibConfiguration->DATABASE_USER; ?>"/><br />
</br>
	<label for=DATABASE_PASS>
	<strong>Main Tagonto Database Password</strong>
	</label> <input type="text" name="DATABASE_PASS" size="50"
	value="<? echo $TagontoLibConfiguration->DATABASE_PASS; ?>"/><br />
</br>
	<label for="DATABASE_TAGONTO_USER_ID">
	Standard tagonto userid
	</label> <input type="text" name="DATABASE_TAGONTO_USER_ID" size="50"
	value="<? echo $TagontoLibConfiguration->DATABASE_TAGONTO_USER_ID; ?>"/><br />
</br>

	<label for="DATABASE_TAGONTO_USERNAME">
	Standard tagonto username
	</label> <input type="text" name="DATABASE_TAGONTO_USERNAME" size="50"
	value="<? echo $TagontoLibConfiguration->DATABASE_TAGONTO_USERNAME; ?>"/><br />
</br>
<p class="submit"><input type="submit" value="Save config" /></p>
</fieldset>




<fieldset>
<legend>TagontoLIB - Step 3: Extra data</legend>
	<label class="first" for="GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD">
	GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD
	</label> <input type="text" name="GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD" size="50"
	value="<? echo $TagontoLibConfiguration->GREEDY_MAPPER_SIGNIFICANCE_THRESHOLD; ?>"/><br />
</br>
	<label for="GREEDY_MAPPER_NOISE_THRESHOLD">
	GREEDY_MAPPER_NOISE_THRESHOLD
	</label> <input type="text" name="GREEDY_MAPPER_NOISE_THRESHOLD" size="50"
	value="<? echo $TagontoLibConfiguration->GREEDY_MAPPER_NOISE_THRESHOLD; ?>"/><br />
</br>
	<label for="STANDARD_MAPPER_WEIGHT_LEVENSHTEIN">
	STANDARD_MAPPER_WEIGHT_LEVENSHTEIN
	</label> <input type="text" name="STANDARD_MAPPER_WEIGHT_LEVENSHTEIN" size="50"
	value="<? echo $TagontoLibConfiguration->STANDARD_MAPPER_WEIGHT_LEVENSHTEIN; ?>"/><br />
</br>
	<label for="STANDARD_MAPPER_WEIGHT_WORDNET">
	STANDARD_MAPPER_WEIGHT_WORDNET
	</label> <input type="text" name="STANDARD_MAPPER_WEIGHT_WORDNET" size="50"
	value="<? echo $TagontoLibConfiguration->STANDARD_MAPPER_WEIGHT_WORDNET; ?>"/><br />
</br>
	<label for="STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER">
	STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER
	</label> <input type="text" name="STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER" size="50"
	value="<? echo $TagontoLibConfiguration->STANDARD_MAPPER_WEIGHT_SIMPLE_CHOOSER; ?>"/><br />
</br>
	<label for="STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD">
	STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD
	</label> <input type="text" name="STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD" size="50"
	value="<? echo $TagontoLibConfiguration->STANDARD_MAPPER_SIGNIFICANCE_THRESHOLD; ?>"/><br />
</br>
	<label for="WORDNET_FIRST_LEVEL_WEIGHT">
	WORDNET_FIRST_LEVEL_WEIGHT
	</label> <input type="text" name="WORDNET_FIRST_LEVEL_WEIGHT" size="50"
	value="<? echo $TagontoLibConfiguration->WORDNET_FIRST_LEVEL_WEIGHT; ?>"/><br />
</br>
	<label for="WORDNET_HIERARCHY_MAX_DEPTH">
	WORDNET_HIERARCHY_MAX_DEPTH
	</label> <input type="text" name="WORDNET_HIERARCHY_MAX_DEPTH" size="50"
	value="<? echo $TagontoLibConfiguration->WORDNET_HIERARCHY_MAX_DEPTH; ?>"/><br />
</br>
	<label for="GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES">
	GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES
	</label> <input type="text" name="GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES" size="50"
	value="<? echo $TagontoLibConfiguration->GOOGLE_CHOOSER_MAX_NEIGHBOUR_PAGES; ?>"/><br />
</br>
	<label for="GOOGLE_CHOOSER_MAX_KEYWORDS">
	GOOGLE_CHOOSER_MAX_KEYWORDS
	</label> <input type="text" name="GOOGLE_CHOOSER_MAX_KEYWORDS" size="50"
	value="<? echo $TagontoLibConfiguration->GOOGLE_CHOOSER_MAX_KEYWORDS; ?>"/><br />
</br>
	<label for="ONTOLOGY_POOL_SIZE">
	ONTOLOGY_POOL_SIZE
	</label> <input type="text" name="ONTOLOGY_POOL_SIZE" size="50"
	value="<? echo $TagontoLibConfiguration->ONTOLOGY_POOL_SIZE; ?>"/><br />
</br>

</fieldset>

</form>
</div>


</div>
	<div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body>
</html>