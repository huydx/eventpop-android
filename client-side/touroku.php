<?php
  //connect db
  $dbhost = '175.184.35.194';
  $dbuser = 'cx0cjz7_ax1';
  $dbpass = 'dxAZEKxH';
  $dbname = 'cx0cjz7_ax1';

  $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("can not connect sql");
  $selectdb = mysql_select_db($dbname, $conn);
  if (!$selectdb) {
    echo "can not select db";  
  }

  $event_name = $_POST['event_name'];
  $event_detail = $_POST['event_detail'];
  $longtitude = $_POST['longtitude'];
  $latitude = $_POST['latitude'];
  $datetime = $_POST['date_time'];

  $datetime = strtotime($datetime);
  $mysqldate = date("Y-m-d H:i:s", $datetime);

  $query = 'insert into events values 
       (null,'.$longtitude.','.$latitude.',"'.$event_detail.'","'.$event_name.'","'.$mysqldate.'")'; 

  $result = mysql_query($query);
  if (!$result) {
    echo $query;
    echo "not success";
  }
  else {
    echo "登録完了";   
  }


?>
