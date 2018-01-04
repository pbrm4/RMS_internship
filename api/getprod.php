<?php 

	
		require_once __DIR__ . '/db_connect.php';
        // connecting to database
        $db = new DB_CONNECT();
        $con = $db->connect(); 	
		
		
		
		$sql = "SELECT DISTINCT productname FROM products";
		
		$r = mysqli_query($con,$sql);
		
		//$res = mysqli_fetch_array($r);
		
		$result = array();
		
		if (mysqli_num_rows($r) > 0) {
			$response["products"] = array();
			while ($row = mysqli_fetch_array($r)) {
				$product = array();
			//	$product["id"] = $row["id"];
				$product["productname"] = $row["productname"];
				
		
				array_push($response["products"], $product);
			}
		
		echo json_encode($response);
		}
		else{
			
			
		}
		mysqli_close($con);
		
	
	?>