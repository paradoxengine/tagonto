<? 
//INCLUSIONS
 require_once "config.php";
 require_once 'classes/PresentationManager.php';

//DECLARATIONS
 $presenter = new presentationManager();
	
	
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<!--  META -->
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	<meta name="keywords" content="tagonto, tag, find, ontology, ontologies" />	<meta name="description" content="An engine exploiting ontologies in finding tagged contents" />
<!--  STYLES -->
	<link href="css/styleTagonto.css" rel="stylesheet" type="text/css" media="screen" />	<link rel="stylesheet" href="css/gwidgets.css" type="text/css" media="screen" title="gWidgets" charset="utf-8">
<!--  JAVASCRIPTS -->
	<script src="js/prototype.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/base.js" type="text/javascript" charset="utf-8"></script>	<script src="js/gwidgets.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/tagonto.js" type="text/javascript" charset="utf-8"></script>
	

<script type="text/javascript">
var gWidget_Options = {declarative:  'true'};
</script>



<title>Tagonto</title>
</style>
</head>
<body>




	<div id="pageWidth">
		<?php include("includes/topAndformInclude.php"); ?>
		
		<!--  Begin results area -->
	<div id="resultsArea">
		
		<p> Welcome in Tagonto, a tag engine based on ontologies. </p>
		
		<p class="generalInfo">View here contents tagged with the searched keyword.</p>	<!--  Begin GTAB DIV -->
	<div id="tabtab" class="gtab" >
			
			<!-- empty div filled through ajax on search -->		</div> 
		
		<!--  End GTAB DIV -->
	<!-- 	<p>You searched for: <span class="object">		</span></p>-->
		<!--
		<p><form action="resultShower.php" method="POST">				That's not what you expected? Improve your search, insert a synonim:<input type="text" name="tagSyn" />				<input type="submit" value="GO!"/>			</form>		</p> 
		--> 		</div>
	
<hr/>
		<!--  End results area -->		
		<?php include("includes/ontologyPartInclude.php"); ?>
	</div>
					<div id="loadingImg" style="	position: fixed;
	bottom: 0;
	padding: 0;
	height: 20px;
	margin: 0;
	width: 100%;
	background-color: #FFFF99;
	display: block;
	text-align: center;
	font-weight: bold;
	font-size: 1.3em;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	padding-top: 5px;
	padding-bottom: 5px;
	display:none;"><img alt="spinner" id="spinner" src="img/loading.gif" 
	      />Please wait while loading....
	 </div>
	 
	 <div id="footer">Developed by Claudio Criscione, Mauro Luigi Drago, Silvia Bindelli for Politecnico di Milano</div>
</body></html>