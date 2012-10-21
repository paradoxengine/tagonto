<?php

//INCLUSIONS
 require_once "../config.php";
?>

		<p>You searched for: <span class="object"><? echo $_GET["tag"]; ?>
		</span></p>


 <!-- <div id="tabtab" class="gtab" > --> 

			<ul>
			<? 
			//Here we will echo the headings
			
			//Retrieve pluginlist
			$availplugins = array();
			$url = $TAGONTONET_REST_AJAX_URL . 'r=ListPlugins';
			$ch = curl_init($url);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
			$result=curl_exec ($ch);

			curl_close ($ch);
				//Parse the xml document
			$domdoc = new DOMDocument;
			$domdoc->loadXML($result);
				//Simple					
	  		$aData = array();
	  		dom_to_simple_array($domdoc, $aData); 
			
	  		//The xml document has been parsed. Building array of pluginnames
	  		foreach($aData['tagonto'][0]['resource'] as $res) {
	  			$availplugins[] = $res['name'][0]['cdata'];
	  		}
	
			$plcount = 0;
			//Getting pl data
			foreach($availplugins as $plug) {
				echo '<li><a href="';
				echo $TAGONTONET_XSL_TRANSLATOR;
				echo '?urltoget=' .urlencode($TAGONTONET_REST_AJAX_URL . 'r=GetResources&tag='.$_GET["tag"] .'&pl='. $plug) .'#container' . $plcount ; //Ajax call
				echo '">';
				echo $plug;
				echo '</a></li>';
				echo "\n\r";
				$plcount++;
			}		
			?>

			</ul>
			<? 
				//Echoing empty divs
				$plcount=0;
				foreach($availplugins as $plug) {
					echo '<div id="';
					echo 'container'.$plcount;
					echo '"></div>';
					$plcount ++;
				}
			?>
<!--  </div>  --> 

		<!--  End GTAB DIV -->
