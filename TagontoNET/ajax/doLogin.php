<?php

/**
* Simple script managing user logins
***/

require_once('../config.php');

//Acquiring user input

$username = $_GET['user'];
$password = $_GET['pass'];
$register = $_GET['register'];
$logout = $_GET['logout'];

if(!empty($logout)) {
	doLogout();
	die();
}
	

//Processing login o register
if(empty($register)) {
	doLogin($username,$password);
	die();
}
else {
	doRegister($username,$password);
	die();
}





/**
* doRegister will try to register the user
***/
function doRegister($username,$password) {
	$LoginManager = new LoginManager();
	$registerresult = $LoginManager->register($username,$password);

	if(!$registerresult) 
		registerFailed();
	else
		registerOk();
	}
	
	

	
	/**
	* doLogin will try to login the user
	***/
function doLogin($username,$password) {
	$LoginManager = new LoginManager();
	$loginresult = $LoginManager->login($username,$password);

	if(!$loginresult) 
		loginFailed();
	else
		loginOk();
 }

 
	
/**
* loginFailed will output a login failed message in the login div :(
***/	
function registerOk() {
	echo '<hr/>';
	echo '<p><strong>Registration ok!</strong>You can now login</p>';
	echo '<table>';
	echo '<tr><td>User </td> <td><input type="text"  id="usrfield" name="usr" /></td> </tr>';
	echo '<tr><td>Password </td> <td><input type="text"  id="pwdfield" name="psw" /></td> </tr>';
	echo '<tr><td></td><td><input type="submit" value="Login" onclick="doLogin();"/></td></tr>';
	echo '</table>';
	echo '<hr/>';
	
}

/**
* loginFailed will output a login failed message in the login div :(
***/	
function loginOk() {
	echo '<hr/>';
	echo '<p><strong>Login ok!</strong>Welcome my old friend';
	echo '<p><a onclick="doLogout();"/><strong>Logout</strong></a></p>';
	echo '<hr/>';
	
}
 

/**
* loginFailed will output a login failed message in the login div :(
***/	
function registerFailed() {
	echo '<hr/>';
	echo '<p><strong>Registration failed.</strong>Try again or login</p>';
	echo '<table>';
	echo '<tr><td>User </td> <td><input type="text"  id="usrfield" name="usr" /></td> </tr>';
	echo '<tr><td>Password </td> <td><input type="text"  id="pwdfield" name="psw" /></td> </tr>';
	echo '<tr><td><input type="submit" value="Login" onclick="doLogin();"/></td><td><input type="submit" value="Register" onclick="doRegister();"/></td></tr>';
	echo '</table>';
	echo '<hr/>';
	
}
 
/**
* loginFailed will output a login failed message in the login div :(
***/	
function loginFailed() {
	echo '<hr/>';
	echo '<p><strong>Login failed.</strong>Try again.</p>';
	echo '<table>';
	echo '<tr><td>User </td> <td><input type="text"  id="usrfield" name="usr" /></td> </tr>';
	echo '<tr><td>Password </td> <td><input type="text"  id="pwdfield" name="psw" /></td> </tr>';
	echo '<tr><td><input type="submit" value="Login" onclick="doLogin();"/></td><td><input type="submit" value="Register" onclick="doRegister();"/></td></tr>';
	echo '</table>';
	echo '<hr/>';
	
}

/**
* doLogout will logout the user and output a login field
**/
function doLogout() {
	$LoginManager = new LoginManager();
	$registerresult = $LoginManager->logout();
	echo '<hr/>';
	echo '<p><strong>Logout complete.</strong> Goodbye</p>';
	echo '<table>';
	echo '<tr><td>User </td> <td><input type="text"  id="usrfield" name="usr" /></td> </tr>';
	echo '<tr><td>Password </td> <td><input type="text"  id="pwdfield" name="psw" /></td> </tr>';
	echo '<tr><td><input type="submit" value="Login" onclick="doLogin();"/></td><td><input type="submit" value="Register" onclick="doRegister();"/></td></tr>';
	echo '</table>';
	echo '<hr/>';
}

?>