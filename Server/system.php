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


// Import the Slim Framework
require "slim-framework/Slim/Slim.php";
\Slim\Slim::registerAutoloader();


// Initiate API object
// $API will be referencing to the Slim Framework object
$API = new \Slim\Slim();


// Define paths
// This is a GET path to example.com/register
// Registers a new consumer app
$API->get('/register', function () {
	$user_id = 1;
	// This should come from a form filled in by the requesting user
	$consumer = array(
	    // These two are required
	    'requester_name' => 'John Doe',
	    'requester_email' => 'john@example.com'
	
	    // These are all optional
	    /*'callback_uri' => 'http://example.com/oauth_callback',
	    'application_uri' => 'http://example.com/',
	    'application_title' => 'Example consumer app',
	    'application_descr' => 'To test out the server',
	    'application_notes' => '',
	    'application_type' => 'website',
	    'application_commercial' => 0*/
	);
	// Register the consumer
	$store = OAuthStore::instance(); 
	$key   = $store->updateConsumer($consumer, $user_id);
	// Get the complete consumer from the store
	$consumer = $store->getConsumer($key, $user_id);

	// The tokens
	$tokens = array(
		'consumer_key'    => $consumer['consumer_key'],
		'consumer_secret' => $consumer['consumer_secret']
	);
	// Set content type to JSON
    header("Content-Type: application/json");
    // Output JSON
	echo json_encode($tokens);
});


// This is a GET path to example.com/request_token
$API->get('/request_token', function () {
	$server = new OAuthServer();
	$server->requestToken();
});


// This is a GET path to example.com/access_token
$API->get('/access_token', function () {
	$server = new OAuthServer();
	$server->accessToken();
});




// This is a GET path to example.com/hello-to/:name
// PATH PARAMETER: :name - a name to echo out
$API->get('/hello-to/:name', function ($name) {
	// Make an assosiative array for the JSON object
	$greeting = array(
		'message' => 'Hey there '.$name.'!'
	);
	// Set content type to JSON
    header("Content-Type: application/json");
    // Output JSON
	echo json_encode($greeting);
});


// This is a GET path to example.com/hello-to/:name
// PATH PARAMETER: :name - a name to echo out
$API->get('/require-auth/:name', function ($name) {
		if (OAuthRequestVerifier::requestIsSigned()) {
	    try {
	        $req = new OAuthRequestVerifier();
	        $user_id = $req->verify();
	
	        // If we have an user_id, then login as that user (for this request)
	        if ($user_id) {
	            // Make an assosiative array for the JSON object
				$greeting = array(
					'message' => 'Hey there '.$name.'!'
				);
				// Set content type to JSON
    			header("Content-Type: application/json");
    			// Output JSON
				echo json_encode($greeting);
				return;
	        }
	    } catch (OAuthException $e) {
	        // The request was signed, but failed verification
	        header('HTTP/1.1 401 Unauthorized');
	        header('WWW-Authenticate: OAuth realm=""');
	        header('Content-Type: text/plain; charset=utf8');
	                                
	        echo $e->getMessage();
	        exit();
	    }
	}
});

/*
*Returns in JSON format a list with all the recipes in the Data Base
*/
$API->get('/list', function(){
              $host="localhost"; // Host name
              $username="edjocorz";//"tooshilt"; // Mysql username
              $password="mysql"; // Mysql password
              $db_name="edjocorz";//"tooshilt"; // Database name
              mysql_connect("$host", "$username", "$password")or die("cannot connect");
              mysql_select_db("$db_name")or die("cannot select DB");
              $sql="SELECT title FROM recipes";
              $rs = mysql_query($sql);
			$rows = array();
			while($r = mysql_fetch_assoc($rs)) {
   				 $rows[] = $r;
			}
          // Set content type to JSON
          header("Content-Type: application/json");
          // Output JSON
          echo json_encode($rows);
});

/*
*Returns in a JSON Format a recipe with the name "title"
*/
$API->get('/recipe/:title',function($title){
          $host="localhost"; // Host name
          $username="edjocorz";//"tooshilt"; // Mysql username
          $password="mysql"; // Mysql password
          $db_name="edjocorz";//"tooshilt"; // Database name
          mysql_connect("$host", "$username", "$password")or die("cannot connect");
          mysql_select_db("$db_name")or die("cannot select DB");
          $title=substr($title,1);
          $sql="SELECT * FROM recipes WHERE title='$title'";
          $rs = mysql_query($sql);
          $row=mysql_fetch_row($rs);
          $result = array(
                          'title' => $row[0],
                          'recipe' => $row[2],
                          'author' => $row[1]
                          );
          // Set content type to JSON
          header("Content-Type: application/json");
          // Output JSON
          echo json_encode($result); //
});
    
    
    //Recive petition of upload a recipe. Just the authorizate applications can do it.
$API->post('/upload', function () {
                         if (OAuthRequestVerifier::requestIsSigned()) {
                         try {
                         $req = new OAuthRequestVerifier();
                         $user_id = $req->verify();
                         // If we have an user_id, then login as that user (for this request)
                         if ($user_id) { //There are a user identify , who try to upload a recipe.
                				echo "AUTHORIZADO";
                				//MAKE THE CONECTION WITH THE DATABASE
                				$host="localhost"; // Host name
       						 	 $username="edjocorz";//"tooshilt"; // Mysql username
         						 $password="mysql"; // Mysql password
       							 $db_name="edjocorz";//"tooshilt"; // Database name
        						mysql_connect("$host", "$username", "$password")or die("cannot connect");
          						mysql_select_db("$db_name")or die("cannot select DB");
          						//OBTAIN THE NAME OF THE USER WHO upload the recipe
         						$sql0="SELECT name FROM users where user_id='$user_id'";
          						$rs = mysql_query($sql0);
         						$row=mysql_fetch_row($rs);
         						$user=$row[0];
         						$recipe=$_POST['recipe'];
         						$title=$_POST['title'];
         						echo $title;
         						//UPLOAD THE RECIPE
                         		$sql="INSERT INTO recipes (title, user, instruction) VALUES ('$title','$user','$recipe')";
                            	$rs = mysql_query($sql);        
                         return;
                         }
                         } catch (OAuthException $e) {
                         // The request was signed, but failed verification
                         header('HTTP/1.1 401 Unauthorized');
                         header('WWW-Authenticate: OAuth realm=""');
                         header('Content-Type: text/plain; charset=utf8');
                         echo $e->getMessage();
                         exit();
                         }
    }
});

 //Recives petition of upload a recipe. Just the authorizate applications can do it.
 //It recives a JSON object.
$API->post('/uploadJSON', function () {
                         if (OAuthRequestVerifier::requestIsSigned()) {
                         try {
                         $req = new OAuthRequestVerifier();
                         $user_id = $req->verify();
                         // If we have an user_id, then login as that user (for this request)
                         if ($user_id) { //There are a user identify , who try to upload a recipe.
                				echo "AUTHORIZADO";
                				//MAKE THE CONECTION WITH THE DATABASE
                				$host="localhost"; // Host name
       						 	 $username="edjocorz";//"tooshilt"; // Mysql username
         						 $password="mysql"; // Mysql password
       							 $db_name="edjocorz";//"tooshilt"; // Database name
        						mysql_connect("$host", "$username", "$password")or die("cannot connect");
          						mysql_select_db("$db_name")or die("cannot select DB");
          						//OBTAIN THE NAME OF THE USER WHO upload the recipe
         						$sql0="SELECT name FROM users where user_id='$user_id'";
          						$rs = mysql_query($sql0);
         						$row=mysql_fetch_row($rs);
         						$user=$row[0];
         						//DECODE THE JSON object
         						$obj = json_decode($_POST['data']);
								$recipe= $obj->{'recipe'}; 
         						$title= $obj->{'title'};         						
         						
         						//UPLOAD THE RECIPE
                         		$sql="INSERT INTO recipes (title, user, instruction) VALUES ('$title','$user','$recipe')";
                            	$rs = mysql_query($sql);        
                         return;
                         }
                         } catch (OAuthException $e) {
                         // The request was signed, but failed verification
                         header('HTTP/1.1 401 Unauthorized');
                         header('WWW-Authenticate: OAuth realm=""');
                         header('Content-Type: text/plain; charset=utf8');
                         echo $e->getMessage();
                         exit();
                         }
    }
});

    
    
// Add a missing path handler
$API->notFound(function () use ($API) {
	echo "This path does not exist!<br />";
});

              


// Run the API
$API->run();

?>