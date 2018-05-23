/**
 * Welcomes the user to the application. Shows the user their logged in status.
 *
 * 
 *@ngdoc controller
 *@name HomeController
 *@memberof tracker
 */
app.controller('HomeController', function ($scope, $rootScope) {
	$scope.isLoggedOut = $rootScope.isLoggedin;
});