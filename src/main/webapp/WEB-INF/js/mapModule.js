/**
 * The main angular module. Has application wide session validation. Can redirect based on authentication state.
 * 
 *  
 * @name tracker
 * 
 */

var app = angular.module("tracker", [ 'ngRoute', 'ngAnimate', 'ui.bootstrap', 'ModelModule' ]).run(
		function($rootScope, $location, model, $http, CONSTANTS) {
			// register listener to watch route changes
			$rootScope.$on("$routeChangeStart", function(event, next, current) {
				// if the session is valid, continue
				$http.get('/getSessionAttributeLoggedin').success(
						function(data, status) {
							$rootScope.isLoggedin = data;
							console.log("Is the user logged in " + data + " The status is " + status);

							if (data === true) {
								model.auth.username = JSON.parse(model.auth.getLocalStorage("username"));
								$http.defaults.headers.common.Authorization = model.auth
										.getLocalStorage("Authorization");

								redirectByRole = function() {
									model.auth.getRoleByUsername().$promise.then(function(role) {
										if (role.roles) {
											if (role.roles.indexOf(CONSTANTS.ROLES.EDITOR) !== -1) {
												$location.path("/editor/profile");
											} else {
												$location.path("/secure/profile");
											}
										} else {
											$location.path("/secure/profile");
										}
									}, function(error) {
										console.log("Error ");
										console.log(error);
										$location.path("/secure/profile");
									});
								}

								if (next.templateUrl === "html/profile.html") {
									redirectByRole();
								}

								if (next.templateUrl === "html/editor/profile.html") {
									redirectByRole();
								}

								if (next.templateUrl === "html/secure/profile.html") {
									redirectByRole();
								}

							} else {
								model.auth.setLocalStorage("username", JSON.stringify(null));
								model.auth.username = null;
								model.auth.setLocalStorage("Authorization", null);
								if (next.templateUrl === "html/editor/profile.html"
										|| next.templateUrl === "html/secure/profile.html") {
									$location.path("/login");
								}
							}
						}).error(
						function(data, status) {
							$rootScope.isLoggedin = false;
							console.log("Is the user logged in " + data + " The status is " + status);
							model.auth.setLocalStorage("username", JSON.stringify(null));
							model.auth.setLocalStorage("Authorization", null);
							model.auth.username = null;
							if (next.templateUrl === "html/editor/profile.html"
									|| next.templateUrl === "html/secure/profile.html") {
								$location.path("/login");
							}
						});
			});
		}).config(function($compileProvider, $httpProvider) {
	// TODO uncomment for production speedHack
	// $compileProvider.debugInfoEnabled(false);

	// speedHack execute nearby digest calls just once using a zero timeout
	$httpProvider.useApplyAsync(true);
}).constant('CONSTANTS', {
	MAP_COORD : {
		lat : 33.4484,
		lng : -112.0740
	},
	COOK_ICON : '/pics/cook_logo.png',
	DEFAULT_ICON : 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FE7569',
	EMPLOYEE_CATEGORY : 'employee',
	OFFICE_CATEGORY : 'office',
	CONVERT_URL_PRE : 'http://maps.googleapis.com/maps/api/geocode/json?address=',
	CONVERT_URL_SUF : '&sensor=false',
	SHOW : 'Show',
	HIDE : 'Hide',
	ROLES : {
		EDITOR : "EDITOR"
	}
})
