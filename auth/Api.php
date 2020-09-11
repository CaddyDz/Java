<?php
// getting the database connection
require_once 'DbConnect.php';

if (isset($_GET['apicall'])) {
	die('reached');

	switch ($_GET['apicall']) {

		case 'login':

			$phone = $_POST['phone'];
			$password = $_POST['password'];

			// creating the query
			$stmt = $conn->prepare("SELECT id, phone, state FROM users WHERE phone = ? AND password = ? AND state = ?");
			$stmt->bind_param("sss", $username, $password, false);

			$stmt->execute();

			$stmt->store_result();

			// if the user exist with given credentials
			if ($stmt->num_rows > 0) {

				$stmt->bind_result($id, $phone, $state);
				$stmt->fetch();

				$user = array(
					'id' => $id,
					'phone' => $phone,
					'state' => $state
				);
			}

			break;

		case 'logout':
			// edit state to false again
			break;
	}
}


function isTheseParametersAvailable($params)
{
	// traversing through all the parameters
	foreach ($params as $param) {
		// if the paramter is not available
		if (!isset($_POST[$param])) {
			//return false
			return false;
		}
	}
	// return true if every param is available
	return true;
}
