<?php 

	
		require_once __DIR__ . '/db_connect.php';
        // connecting to database
        $db = new DB_CONNECT();
        $con = $db->connect(); 	
		
		
		if(isset($_GET["lat"])&&isset($_GET["lon"])){
		    
		    $lat = floatval($_GET["lat"]);
		    $lon = floatval($_GET["lon"]);
	//	$sql = "SELECT DISTINCT retailname FROM products";
		$sql = "SELECT DISTINCT retailname FROM products where (sqrt(($lat-latitude)*($lat-latitude)+($lon-longitude)*($lon-longitude)) <0.1)";
		
		$r = mysqli_query($con,$sql);
		
		//$res = mysqli_fetch_array($r);
		
		$result = array();
		
		if (mysqli_num_rows($r) > 0) {
			$response["products"] = array();
			while ($row = mysqli_fetch_array($r)) {
				$product = array();
			//	$product["id"] = $row["id"];
				$product["retailname"] = $row["retailname"];
				
		
				array_push($response["products"], $product);
			}
		
		echo json_encode($response);
		}
		
		
		else{
			
			
		}
		}
		else{}
		mysqli_close($con);
		
	
	?>