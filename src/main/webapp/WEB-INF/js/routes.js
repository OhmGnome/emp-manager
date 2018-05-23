/**
 * listens for path changes and directs ng-view accordingly
 *  
 *  
 * @memberof tracker
 * @name trackerConfig
 * 
 */
app.config(function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl : '/html/login.html',
		controller : 'LoginController'
	}).when('/home', {
		templateUrl : '/html/home.html',
		controller : 'HomeController'
	}).when('/login', {
		templateUrl : 'html/login.html',
		controller : 'LoginController'
	}).when('/logout', {
		templateUrl : 'html/logout.html',
		controller : 'LoginController'
	}).when('/map', {
		templateUrl : '/html/map.html',
		controller : 'MapController'
	}).when('/profile', {
		templateUrl : 'html/profile.html',
		controller : 'ProfileController'
	}).when('/secure/profile', {
		templateUrl : 'html/secure/profile.html',
		controller : 'SecureProfileController'
	}).when('/editor/profile', {
		templateUrl : 'html/editor/profile.html',
		controller : 'SecureProfileController'
	}).when('/registration', {
		templateUrl : '/html/registration.html',
		controller : 'RegistrationController'
	}).when('/registration/:authId', {
		templateUrl : 'html/registrationSuccess.html',
		controller :  'RegistrationController'
	}).otherwise({
		redirectTo : '/home'
	});
});