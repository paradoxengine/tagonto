  <div id="ontologyArea">
	<h2>Ontology surfing</h2>




	<? //HERE WE GENERATE A loginArea if the user is not logged in, otherwise a "Welcome screen and logout area" 
		$LoginManager = new LoginManager();
		if(!$LoginManager->isLoggedin()) {
	?>
		<div id="loginArea">
			<hr/>
			<p>Login here to interact with ontology</p>
			<table>
				<tr><td>User </td> <td><input type="text" id="usrfield" name="usr" /></td> </tr>
				<tr><td>Password </td> <td><input type="text" id="pwdfield" name="psw" /></td> </tr>
				<tr><td><input type="submit" value="Login" onclick="doLogin();" /></td><td><input type="submit" value="Register" onclick="doRegister();"/></td></tr>
			</table>
			<hr/>
		</div>
	<?
		}	
		else {
	?>
		<div id="loginArea">
			<hr/>
			<p>Welcome in Tagonto</p>
			<p><strong><a onclick="doLogout();"/>Logout</a></strong></p>
			<hr/>
		</div>
		
	<?
		}
	?>
					
			<div id="leftPart">
			
			<div id="ontologyDescription" >
				In this section you can explore the loaded ontology, moving among its concepts and mapping tags on them.
			</div>

			<p class="generalInfo">View here Ontology concepts tagged with the searched keyword.</p>
			
				<div id="disambiOntologyContainer">
					<img alt="spinner" id="spinner" src="img/loading.gif" 
	      style="display:none;" />
					<!--  this empty div is filled by the disambiguation function -->
				</div>
				
				
			</div>
		</div>
		

