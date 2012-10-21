<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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

<!--
<div id="resources">
<xsl:for-each select="tagonto/resource/">
<xsl:sort data-type="text" select="url"/>

   <a>
    <xsl:attribute name="href">
  	<xsl:value-of select="url" />
    </xsl:attribute> 
    <img height="50px" width="50px">
  		<xsl:attribute name="src">
  		<xsl:value-of select="logo" />
  		</xsl:attribute>      
   	</img>
   </a>
   <a>
    <xsl:attribute name="href">
  	<xsl:value-of select="url" />
    </xsl:attribute> 
    <xsl:value-of select="title" />       
   </a><br/>
   
   <xsl:value-of select="desc" />
-->
	<div id="results">
	<xsl:for-each select="tagonto/resource/results/result">
	<p>
   		<a>
    		<xsl:attribute name="href">
  			<xsl:value-of select="url" />
    		</xsl:attribute> 
    		<xsl:value-of select="title" />
   		</a><br/>
   
   		<xsl:value-of select="desc" />
 
   		<xsl:if test="type = 1">
   		<img height="50px" width="50px">
  			<xsl:attribute name="src">
  			<xsl:value-of select="url" />
  			</xsl:attribute>      
   		</img>
  		</xsl:if>
	</p>
	</xsl:for-each>
	</div>
<!--
</xsl:for-each>
</div>-->
</body>
</html>
</xsl:template>
</xsl:stylesheet>