<?php
    
    $host="localhost"; // Host name
    $username="edjocorz";//"tooshilt"; // Mysql username
    $password="mysql"; // Mysql password
    $db_name="edjocorz";//"tooshilt"; // Database name
    $tbl_name="users"; // Table name
    // Connect to server and select databse.
    mysql_connect("$host", "$username", "$password")or die("cannot connect");
    mysql_select_db("$db_name")or die("cannot select DB");
    
    // username and password sent from form
    $myusername=$_POST['myusername'];
    $mypassword=$_POST['mypassword'];
    $sql="SELECT * FROM $tbl_name WHERE name='$myusername' AND password='$mypassword'";
    $result=mysql_query($sql);
    $count=mysql_num_rows($result);
    if($count==1){
        echo "Wellcome";
        $url="http://itks545.it.jyu.fi/edjocorz/menu";
         echo "<SCRIPT>window.location='$url';</SCRIPT>";
    }
    else{
        echo "Wrong Username or Password";
    }
    
?>