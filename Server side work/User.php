<?php
$uid = $_GET['uid'];
$locid = $_GET['locid'];
$locname = $_GET['locname'];
$rating = intval($_GET['rating']);
$comment = $_GET['com'];
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
 
//$query = "SELECT * FROM drivername where driver_id=".$id;
$query = "INSERT INTO  `a1780121_ola`.`userdata` (
`uid` ,
`locid` ,
`locname` ,
`rating` ,
`comment`
)
VALUES (
'$uid',  '$locid',  '$locname',  '$rating',  '$comment'
)";
$result = mysqli_query($conn,$query);
if(! $result )
{
  die('Could not enter data: ' . mysql_error());
}
//$row = mysqli_fetch_assoc($result);
/*$contents = $row['driver_name'];
if($contents == null)
echo "Error";
else
echo $contents;*/
echo "Entered data successfully\n";
mysqli_close($conn);
 
?>