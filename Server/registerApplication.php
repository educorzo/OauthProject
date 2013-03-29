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
   // require "slim-framework/Slim/Slim.php";
   // \Slim\Slim::registerAutoloader();
    
    // Initiate API object
    // $API will be referencing to the Slim Framework object
    //$API = new \Slim\Slim();
    

    $host="localhost"; // Host name
    $username="edjocorz";//"tooshilt"; // Mysql username
    $password="mysql"; // Mysql password
    $db_name="edjocorz";//"tooshilt"; // Database name
    $tbl_name="users"; // Table name
    // Connect to server and select database.
    mysql_connect("$host", "$username", "$password")or die("cannot connect");
    mysql_select_db("$db_name")or die("cannot select DB");
    
    
    // username and password sent from form
    $requester_name=$_POST['myusername'];
    $mypassword=$_POST['mypassword'];
    $requester_email=$_POST['myemail'];
    $application_title=$_POST['mytitle'];
    $sql="SELECT * FROM $tbl_name WHERE name='$requester_name' AND password='$mypassword'";
    $result=mysql_query($sql);
    $count=mysql_num_rows($result);
    $rs=mysql_query("SELECT user_id FROM $tbl_name WHERE name='$requester_name'");
    if ($row = mysql_fetch_row($rs)) {
        $id = trim($row[0]);
    }
    $user_id=$id;
    if($count==1){ //IF THE USER EXIST
        $consumer = array(
                          // These two are required
                          'requester_name' => $requester_name,
                          'requester_email' => $requester_email,
                          'application_title' => $application_title
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
        
    }
    else{ //IF THE USER DOESN'T EXIST
        echo "INCORRECT USER OR PASSWORD";
    }

?>