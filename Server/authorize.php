<?php

// Initialize OAuth server & store
require_once 'oauth-php/library/OAuthServer.php';
require_once 'oauth-php/library/OAuthStore.php';
OAuthStore::instance('MySQL', array(
	'server'   => 'localhost',
	'username' => 'edjocorz',
	'password' => 'mysql',
	'database' => 'edjocorz'
));
    $host="localhost"; // Host name
    $username="edjocorz";//"tooshilt"; // Mysql username
    $password="mysql"; // Mysql password
    $db_name="edjocorz";//"tooshilt"; // Database name
    $tbl_name="users"; // Table name
    $store  = OAuthStore::instance();
	$server = new OAuthServer();
try {
              // Check if there is a valid request token in the current request
              // Returns an array with the consumer key, consumer secret, token, token secret and token type.
            $rs = $server->authorizeVerify();
			$consumer_key=$rs['consumer_key'];
           //Extract the name of the Application
           mysql_connect("$host", "$username", "$password")or die("cannot connect");
        mysql_select_db("$db_name")or die("cannot select DB");
        $sql="SELECT osr_application_title FROM oauth_server_registry WHERE osr_consumer_key='$consumer_key'";
         $result=mysql_query($sql);
         $row = mysql_fetch_row($result);
        $nameApp = trim($row[0]);
           
              if ($_SERVER['REQUEST_METHOD'] == 'POST'){
                  // Connect to server and select databse.
                  mysql_connect("$host", "$username", "$password")or die("cannot connect");
                  mysql_select_db("$db_name")or die("cannot select DB");
                  // username and password sent from form
                  $myusername=$_POST['myusername'];
                  $mypassword=$_POST['mypassword'];
                  $sql="SELECT user_id FROM $tbl_name WHERE name='$myusername' AND password='$mypassword'";
                  $result=mysql_query($sql);
                  $count=mysql_num_rows($result);
                  if($count==1){
                  	$row = mysql_fetch_row($result);
                 	$user_id = trim($row[0]);
                    $store  = OAuthStore::instance();
					$server = new OAuthServer();
                    $authorized = true;
	        		$server->authorizeFinish($authorized, $user_id);
          			echo "You authorize this application";
          			close();
                  }
                  else{
                      echo "Wrong Username or Password";
                  }
              }else{
              echo "<center><strong>
     				 Do you allow <h2>$nameApp</h2> ?
      				</strong></center>";
                ?>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <table width="300" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#CCCCCC">
  				  <tr>
                    <form  method='POST'>
          			  <td>
                		<table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                    		<tr>
                        		<td width="78">Username</td>
                        		<td width="6">:</td>
                        		<td width="294"><input name="myusername" type="text" id="myusername"></td>
                    		</tr>
                    		<tr>
                        		<td>Password</td>
                        		<td>:</td>
                        		<td><input name="mypassword" type="text" id="mypassword"></td>
                    		</tr>
                    		<tr>
                        		<td>&nbsp;</td>
                        		<td>&nbsp;</td>
                        	<td><input type="submit" name="Submit" value="Allow"></td>
                    		</tr>
                		</table>
            			</td>
            		</form>  
            		</tr>
				</table>
                <?
				}//else

} catch (OAuthException $e) {
    // No token to be verified in the request, show a page where the user can enter the token to be verified
    // **your code here**
    echo "ERRORR";
}

?>