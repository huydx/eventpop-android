<?php
  $ret = array('events' => array());

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

  $query = 'select * from events';
  
  $result = mysql_query($query);
  if (!$result) {
    echo('Invalid query: ' . mysql_error());
  }
  
  while ($row = mysql_fetch_assoc($result)) {
    $temp = array();
    $temp['event_id'] = $row['event_id'];
    $temp['event_content'] = $row['event_content'];
    $temp['event_date']  = $row['event_date'];
    $temp['event_name'] = $row['event_name'];
    $temp['longtitude'] = $row['longtitude'];
    $temp['latitude'] = $row['latitude'];
 
    array_push($ret['events'], $temp);
  }
  
  //return event info
  echo json_encode($ret);
?>

