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
    


  /*  // Connect to server and select databse.
    mysql_connect("$host", "$username", "$password")or die("cannot connect");
    mysql_select_db("$db_name")or die("cannot select DB");
    
    // username and password sent from form
    $myusername=$_POST['myusername'];
    $mypassword=$_POST['mypassword'];
    $myemail=$_POST['myemail'];
    $sql="SELECT * FROM $tbl_name WHERE name='$myusername'";
    $result=mysql_query($sql);
    $count=mysql_num_rows($result);
    if($count==1){
        echo "You need to use another name";
    }
    else{
        $rs = mysql_query("SELECT MAX(user_id) AS id FROM $tbl_name");
        if ($row = mysql_fetch_row($rs)) {
            $id = trim($row[0]);
        }
        $sql2="INSERT INTO $tbl_name VALUES('$myusername','$mypassword','$myemail','$user_id')";
        $result2=mysql_query($sql2);
        if($result2==1){
        echo "You are registered in the system";
        }
        else{
            echo "Something happens , we can't register the user";
        }
    }
    */
    
    // This is a GET path to example.com/hello-to/:name
    // PATH PARAMETER: :name - a name to echo out
    $API->get('/hello/:name', function ($name) {
              // Make an assosiative array for the JSON object
              $greeting = array(
                                'message' => 'Hey there '.$name.'!'
                                );
              // Set content type to JSON
              header("Content-Type: application/json");
              // Output JSON
              echo json_encode($greeting);
              });
    
    $API->get('/list', function($name)){
        
        $host="localhost"; // Host name
        $username="edjocorz";//"tooshilt"; // Mysql username
        $password="mysql"; // Mysql password
        $db_name="edjocorz";//"tooshilt"; // Database name
        mysql_connect("$host", "$username", "$password")or die("cannot connect");
        mysql_select_db("$db_name")or die("cannot select DB");
        $sql="SELECT * FROM recipes";
        echo "hola";
        $rs = mysql_query("SELECT MAX(user_id) AS id FROM $tbl_name");
        for ($i = 1; $i <= mysql_num_rows($rs); $i++) {
            echo trim($row[i]);
        }
    });
    
    // Add a missing path handler
    $API->notFound(function () use ($API) {
                   echo "This path does not exist!<br />";
                   });
    
    
    // Run the API
    $API->run();
?>