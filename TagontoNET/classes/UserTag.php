<?php
	class UserTag extends Tag{
		
		private $userID = null;
		
		function getUserID(){
			return $this->userID;
		}
		
		function setUserID($u){
			$this->userID = $u;
		}
		
	}
?>