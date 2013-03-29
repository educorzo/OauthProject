<?php
    //REGISTER ONE USER
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
    
?>