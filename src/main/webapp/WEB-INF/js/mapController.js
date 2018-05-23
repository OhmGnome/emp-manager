/**
 * Leverages the google maps api and places markers according to the addresses in the offices and users persisted
 * objects.
 * 
 * @memberof tracker
 * @ngdoc controller
 * @name MapController
 */

app.controller("MapController", function($http, $scope, $q, $rootScope, model, CONSTANTS) {

	$scope.employeeButtonText = CONSTANTS.HIDE;
	$scope.officeButtonText = CONSTANTS.HIDE;
	var users, offices;
	$scope.authUser = {};

	// get the current logged in user so we can center the new map on them
	if (model.auth.username) {
		model.auth.getUserByUsername().$promise.then(function(data) {
			$scope.authUser = data;
			if ($scope.authUser.address1) {
				$scope.authUser.address = $scope.authUser.address2 ? $scope.authUser.address1 + ' '
						+ $scope.authUser.address2 : $scope.authUser.address1;
			}
			wrapper();
		})
		// or center the new map on phx
	} else {
		wrapper();
	}

	/**
	 * Wrapped because it is called whether the user is logged in or not. Because one scenario requires a promise, this
	 * function can be called from 2 divergent states. Leverages the google maps api to make a map and place markers.
	 * 
	 * @function wrapper
	 * 
	 */
	function wrapper() {

		$http.get('user/').success(function(data) {
			users = data;
			$http.get('/office/').success(function(data) {
				offices = data;
				checkHTTPComplete();
			});
		});

		function checkHTTPComplete() {
			var i, j;
			var markerInfo = {};
			
			initMap();
			
			if (users){
				for (i = 0; i < users.length; i++) {
					markerInfo.name = users[i].firstname + ' ' + users[i].lastname;
					markerInfo.address = users[i].address2 ? users[i].address1 + ' ' + users[i].address2
							: users[i].address1;
					markerInfo.job = users[i].title;
					markerInfo.experience = users[i].experience;
					createMarkers(markerInfo, i);
				}
			}
			
			if (offices) {
				for (j = 0; j < offices.length; j++) {
					var markerInfo = {};
					markerInfo.name = offices[j].name;
					markerInfo.address = offices[j].address2 ? offices[j].address1 + ' ' + offices[j].address2
							: offices[j].address1;
					markerInfo.icon = CONSTANTS.COOK_ICON;
					markerInfo.category = 'office';
					markerInfo.office = offices[j];
					createMarkers(markerInfo, j);
				}
			}
		}

		var map;
		var markers = [];
		var coords = [];
		var infowindow = new google.maps.InfoWindow();

		function initMap() {
			var mapDiv = document.getElementById('map');
			var initMapCoord = CONSTANTS.MAP_COORD;

			if ($scope.authUser.address) {
				var user = $scope.authUser;
				if (user.lat && user.lng) {
					initMapCoord = new google.maps.LatLng(user.lat, user.lng);
					makeMap(initMapCoord);
				} else {
					$.getJSON(CONSTANTS.CONVERT_URL_PRE + user.address + CONSTANTS.CONVERT_URL_SUF, null, function(data) {
						console.log(data); // important
						initMapCoord = data.results[0].geometry.location;
						user.lat = initMapCoord.lat;
						user.lng = initMapCoord.lng;
						model.user.put(user);

						initMapCoord = new google.maps.LatLng(initMapCoord.lat, initMapCoord.lng);
						makeMap(initMapCoord);
					})
				}
			} else {
				makeMap(initMapCoord);
			}

			function makeMap(initMapCoord) {
				console.log('makeMap function');
				console.log(initMapCoord);
				map = new google.maps.Map(mapDiv, {
					center : initMapCoord,
					zoom : 9
				});

				google.maps.event.addListener(map, 'click', function() {
					infowindow.close();
				});
			}
		}

		function createMarkers(markerInf, index) {
			
			if (markerInf.category === 'office' && offices[index].lat && offices[index].lng) {
				var coord = new google.maps.LatLng(offices[index].lat, offices[index].lng);
				setMarker(coord, markerInf);
			} else if (users[index].lat && users[index].lng) {
				var coord = new google.maps.LatLng(users[index].lat, users[index].lng);
				setMarker(coord, markerInf);
			} else if (markerInf.address){
				$.getJSON(CONSTANTS.CONVERT_URL_PRE + markerInf.address + CONSTANTS.CONVERT_URL_SUF, null, function(data) {
					console.log(markerInf.address);
					console.log(data); // important
					var coord = data.results[0].geometry.location;

					if (markerInf.category === 'office') {
						offices[index].lat = coord.lat;
						offices[index].lng = coord.lng;
						model.office.put(offices[index]);
					} else {
						users[index].lat = coord.lat;
						users[index].lng = coord.lng;
						model.user.put(users[index]);
					}

					coord = new google.maps.LatLng(coord.lat, coord.lng);
					setMarker(coord, markerInf);
				});
			}
		}

			function setMarker(coord, markerInf) {
				console.log(coord);
				var marker = new google.maps.Marker({
					position : coord,
					map : map,
					icon : markerInf.icon ? markerInf.icon : CONSTANTS.DEFAULT_ICON,
					title : markerInf.name,
					job : markerInf.job,
					experience : markerInf.experience,
					address : markerInf.address,
					category : markerInf.category ? markerInf.category : CONSTANTS.EMPLOYEE_CATEGORY
				});
				marker.addListener('click', function() {
					infowindow.setContent("<table border='0'>" + "<thead></thead>" + "<tbody>" + "<tr>"
							+ "<td width='100'>Title</td>" + "<td>"
							+ marker.title
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td width='100'>Organization</td>"
							+ "<td>Cook Systems International</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td width='100'>Department</td>"
							+ "<td>IT</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td width='100'>Address</td>"
							+ "<td>"
							+ marker.address
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td width='100'>Job Title</td>"
							+ "<td>"
							+ marker.job
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td width='100'>Years of Experience</td>"
							+ "<td>"
							+ marker.experience
							+ "</td>"
							+ "</tr>" + "</tbody>" + "</table>" + "<button onclick='send()'>submit</button>");
					infowindow.open(map, marker);
				});
				markers.push(marker);
			}
	}

		$scope.toggleEmployees = function() {
			var vis;
			if ($scope.employeeButtonText === CONSTANTS.HIDE) {
				vis = false;
				$scope.employeeButtonText = CONSTANTS.SHOW;
			} else {
				vis = true;
				$scope.employeeButtonText = CONSTANTS.HIDE;
			}

			var i = 0, len = markers.length;
			for (; i < len; i++) {
				if (markers[i].category === CONSTANTS.EMPLOYEE_CATEGORY) {
					markers[i].setVisible(vis);
				}
			}
		}

		$scope.toggleOffice = function() {
			var vis;
			if ($scope.officeButtonText === CONSTANTS.HIDE) {
				vis = false;
				$scope.officeButtonText = CONSTANTS.SHOW;
			} else {
				vis = true;
				$scope.officeButtonText = CONSTANTS.HIDE;
			}

			var i = 0, len = markers.length;
			for (; i < len; i++) {
				if (markers[i].category === CONSTANTS.OFFICE_CATEGORY) {
					markers[i].setVisible(vis);
				}
			}
		}

	$scope.verify = function(phonenum) {
		$http.post('user/verify', phonenum).success(function(data) {
			$scope.validationCode = data;
		}).error(function() {
			console.log("Phone not verified");
		});
	}

	$scope.send = function(toNum, message, image) {
		var data = [ toNum, message, image ];
		$http.post('user/send', data).success(function() {
		}).error(function() {
			console.log("Failed to send message to " + toNum);
		});
	}

});