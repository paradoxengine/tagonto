<?php header("Content-Type: application/xml"); ?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="/">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<meta name="keywords" content="tagonto, tag, find, ontology, ontologies" />

<meta name="description" content="An engine exploiting ontologies in finding tagged contents" />

<link href="../css/xmlResults.css" rel="stylesheet" type="text/css" media="screen" />
<title>Tagonto</title>

</head>
<body>
	<div class="results">
	<xsl:for-each select="tagonto/resource/results/result">
	<xsl:if test="type != 1">
	<p>
   		<a>
    		<xsl:attribute name="href">
  			<xsl:value-of select="url" />
    		</xsl:attribute> 
    		<xsl:value-of select="title" />
    	</a><br/>
   
   		<xsl:value-of select="desc" />
 	</p>
 	</xsl:if>
	
	 <xsl:if test="type = 1">
   		<a class="gtip">
    		<xsl:attribute name="href">
  			<xsl:value-of select="url" />
    		</xsl:attribute>
    	    <xsl:attribute name="title">
  			<xsl:value-of select="title" />
    		</xsl:attribute>
    		<img height="70px" width="70px">
  				<xsl:attribute name="src">
  				 <xsl:value-of select="showncontent" />
  				</xsl:attribute>
  			</img>
   		</a>
  		</xsl:if>
	</xsl:for-each>
	</div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>