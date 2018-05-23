/**
 * The login controller authenticates the users login attempt and also invalidates the session on the backend when the
 * user logs out. Links a new user to registration page. The email registration function is not working with AWS, 
 * so sign up is disabled.
 * 
 * @memberof tracker
 * @name LoginController
 * @ngdoc controller
 */

app.controller('LoginController', function($scope, $rootScope, model, $http, $log, $location) {
	$log.log('in LoginController');
	$scope.user = {};

	$scope.registrationRedirect = function() {
		$log.log('in registrationRedirect()');
		$location.path('/registration');
	};

	/**
	 * authenticates the user, verifies the session, sets a boolean based on the truthiness of that session, and
	 * redirects to the map page if the session is truthy
	 * 
	 * @memberof LoginController
	 * @function login
	 */
	$scope.login = function(user) {
		console.log('login()');
		$http.post('/authenticateUser', user).success(function(data, status) {
			if (data.isAuthorized = true) {
				console.log('token');
				console.log(data.token);
				model.auth.setLocalStorage("Authorization", data.token);
				$http.defaults.headers.common.Authorization = data.token;
				
				$http.get('/getSessionAttributeLoggedin').success(function(data, status) {
					$rootScope.isLoggedin = data;
					if ($rootScope.isLoggedin) {
						model.auth.username = user.username;
						model.auth.setLocalStorage("username", JSON.stringify(model.auth.username));
						$location.path('/map');
					}
					$log.log("Is the user logged in " + data + " The status is " + status);
				}).error(function(data, status) {
					$log.log("Is the user logged in " + data + " The status is " + status);
				});
			} else {
				$scope.messageLoginAttempt = "Login attempt unsuccessful : Bad credentials"
				$log.log("Is the user logged in " + data + " The status is " + status);
			}
		}).error(function(data, status) {
			$log.log("The user has logged in " + data + " The status is " + status);
		});
	};

	/**
	 * @memberof LoginController
	 * @function logout invalidates the session
	 */
	$scope.logout = function() {
		$http.get('/logout').success(function(data, status) {
			$rootScope.isLoggedin = false;
			model.auth.username = null;
			$location.path('/home');
		}).error(function(data, status) {
			$log.log("The user has logged out " + data + " The status is " + status);
		});
	}
});