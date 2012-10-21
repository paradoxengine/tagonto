<?php

//Starting the session, static way...
	session_start();

/**
 * LoginManager is used to manage user logins 
 */
class LoginManager {
	
	/**
	 * Will setup the session for the user, if permission is granted.
	 * @return true if the login has been sucessefull, false otherwise
	 *
	 */
	public function login($username = '',$password = '') {

		$safeusername = mysql_real_escape_string($username);
		//MD5 of the password
		$safepassword = md5($password);
		//Query
		$query = "SELECT * FROM users WHERE username = '$safeusername' AND password = '$safepassword'";
		
		//Security check
		$queryres = mysql_query($query);
		$queryres = mysql_fetch_assoc($queryres);
		
		
		if($queryres['password'] == $safepassword) {
			$_SESSION['isLogged'] = true;
			$user['id'] = $queryres['id'];
			$user['username'] = $queryres['username'];
			$user['password'] = $queryres['password'];
			$user['significance'] = $queryres['significance'];
			$_SESSION['loggeduser'] = $user;
			return true;
		}

		return false;
	}
	
	
	/**
	 * Will logout the user, destroying the session variable;
	 *
	 */
	public function logout() {
		session_destroy();
		return;
	}
	
	/**
	 * Will return true if the user is actually loggedin, according to the session variable
	 *
	 * @return boolean loggedstatus
	 * 
	 */
	public function isLoggedin() {
		if($_SESSION['isLogged']) return true;
		return false;
	}
	
	/**
	 * Will register a new user
	* @return boolean Registeringsucessfull
	***/
	public function register($username,$password) {
		if(empty($username) || empty($password))
			return false;
		
		$safeusername = mysql_real_escape_string($username);
		$safepassword = md5(mysql_real_escape_string($password));
		
		//Checking if the user is already registered
		$USERVERIFY_QUERY = "SELECT count(*) as esiste FROM users WHERE username ='$safeusername'";
		$userverresult = mysql_query($USERVERIFY_QUERY);
		$var=mysql_fetch_assoc($userverresult);
		if($var['esiste'] > 0)
			return false;
			
		$REGISTRATION_QUERY = "INSERT INTO users (username,password) VALUES ('$safeusername','$safepassword')";
		
		return mysql_query($REGISTRATION_QUERY);;
	
	}
	
	
	/**
	* Will return user informations about the current logged user
	* **/ 
	public function getLoggedUserDetails() {
		if(!$this->isLoggedin())
			return array();
		else
			return $_SESSION['loggeduser'];
	}
	
	
}

?>