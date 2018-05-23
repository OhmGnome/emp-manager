/**
 * Calls on the backend controller to start the email verification process
 * 
 * @ngdoc controller
 * @memberof tracker
 * @name RegistrationController
 * 
 */
app.controller('RegistrationController', function($scope, $log, $http, $routeParams) {
	$log.log('in RegistrationController');

	/**
	 * @memberof RegistrationController
	 * @function registerUser
	 */
	$scope.registerUser = function() {
		$http.post('/newUser', $scope.user).success(function(data, status) {
			$log.log("success");
			$scope.registered = 'Registration verification email sent, but may not have escaped the environment';
		}).error(function(data, header, status) {
			$scope.registered = 'Registration verification email failed to send';
		});
	};
});