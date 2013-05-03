<?php
  $RANGE = 0.00002;
  $ret = array('events' => array());

  $lat = $_POST['lat'];
  $lng = $_POST['lng'];

  //connect db
  $dbhost = '';
  $dbuser = '';
  $dbpass = '';
  $dbname = '';

  $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("can not connect sql");
  $selectdb = mysql_select_db($dbname, $conn);
  if (!$selectdb) {
    echo "can not select db";  
  }

  //process lattitude and longtitude
  $lat_upper = floatval($lat) + $RANGE;
  $lat_lower = floatval($lat) - $RANGE;
  $lng_upper = floatval($lng) + $RANGE;
  $lng_lower = floatval($lng) - $RANGE;
  
  $query = 'select event_id, event_content, event_name, event_date from events
            where longtitude between '.$lng_lower.' and '.$lng_upper.'
            and   latitude between '.$lat_lower.' and '.$lat_upper;

  
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
 
    array_push($ret['events'], $temp);
  }
  
  //return event info
  echo json_encode($ret);
?>
