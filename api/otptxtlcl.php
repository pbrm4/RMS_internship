<?php
	// Authorisation details.
	$username = "befinalanp@gmail.com";
	$hash = "92ecb5085688c60197696af94c3dd7064ed6ab3148da70d1ea050921f32a8000";

	// Config variables. Consult http://api.textlocal.in/docs for more info.
	$test = "0";

	// Data for text message. This is the text message data.
	if(isset($_GET['otp'])&&isset($_GET['phone'])){
	$sender = "TXTLCL"; // This is who the message appears to be from.
	$numbers = $_GET['phone']; // A single number or a comma-seperated list of numbers
	$message = "OTP for Retail Mapping system:   ".$_GET['otp'];
	// 612 chars or less
	// A single number or a comma-seperated list of numbers
	$message = urlencode($message);
	$data = "username=".$username."&hash=".$hash."&message=".$message."&sender=".$sender."&numbers=".$numbers."&test=".$test;
	$ch = curl_init('http://api.textlocal.in/send/?');
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	$result = curl_exec($ch); // This is the result from the API
	curl_close($ch);
	}
	else{
		echo "NO";
	}
?>