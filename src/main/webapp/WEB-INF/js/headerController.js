/**
 * The NavBar controller provides all the functionality of the buttons in the
 * header: navigation.
 * 
 *  
 * @ngdoc controller
 * @name HeaderController
 * @memberof tracker
 * 
 */

app.controller('HeaderController', function($scope, $location, $log) {

	/**
	 * Sets the active class to the current navbar link
	 * 
	 * @memberof HeaderController
	 * @function isActive
	 */
    $scope.isActive = function(route) {
        return route === $location.path();
    }

	/**
	 * redirects to the profile page
	 * 
	 * @memberof HeaderController
	 * @function profileRedirect
	 */
	$scope.profileRedirect = function() {
		$log.log('in profileRedirect()')
		$location.path('profile');
	};

	/**
	 * redirects to the map page
	 * 
	 * @memberof HeaderController
	 * @function mapRedirect
	 */
	$scope.mapRedirect = function() {
		$log.log('in mapRedirect()')
		$location.path('map');
	};

	/**
	 * redirects to the login page
	 * 
	 * @memberof HeaderController
	 * @function loginRedirect
	 */
	$scope.loginRedirect = function() {
		$log.log('in loginRedirect()')
		$location.path('login');
	};

	/**
	 * redirects to the logout page
	 * 
	 * @memberof HeaderController
	 * @function logoutRedirect
	 */
	$scope.logoutRedirect = function() {
		$log.log('in logoutRedirect()');
		$location.path('logout');
	}

	/**
	 * redirects to the home page
	 * 
	 * @memberof HeaderController
	 * @function homeRedirect
	 */
	$scope.homeRedirect = function() {
		$log.log('in homeRedirect()');
		$location.path('home');
	}
});