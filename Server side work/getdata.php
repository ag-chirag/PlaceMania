<?php
//$id = $_GET['id'];
$servername = "mysql4.000webhost.com";
$username = "a1780121_zeeshan";
$password = "premchopra123";
// Create connection
$conn = mysqli_connect($servername, $username, $password);
 
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
 
// select database
$selected = mysqli_select_db($conn,"a1780121_ola") 
  or die("sorry, not able to connect");
 
$result = mysqli_query($conn,"SELECT * from userdata;")
or die ('cannot select table fields');
 
$numrows=mysqli_num_rows($result);
 
while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC))
{
$myarray[]=$row;
}
 
echo json_encode($myarray);
 
mysqli_close($conn);
?>