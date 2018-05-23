/**
 * An http request for authentication
 * 
 *  
 * @memberof ModelModule
 * @ngdoc service
 * @name authService
 * @returns User
 */
modelModule.service("authService", function($resource, $http, $rootScope) {
	this.username = null;

	/**
	 * @memberof authService
	 * @function getUserByUsername
	 * @returns User the authorized user
	 */
	this.getUserByUsername = function() {
		if ($rootScope.isLoggedin) {
			return $resource('/user/name/:username').get({
				username : this.username
			});
		}else {
			this.username = null;
		}
	}
	
	/**
	 * @memberof authService
	 * @function getUserByUsername
	 * @returns Role of the authorized user
	 */
	this.getRoleByUsername = function() {
		if ($rootScope.isLoggedin) {
			return $resource('/user/role/:username').get({
				username : this.username
			});
		}else {
			this.username = null;
		}
	}
	
	this.getLocalStorage = function(property){
		return window.localStorage.getItem(property);
	}
	
	this.setLocalStorage = function(property, value){
		window.localStorage.setItem(property, value);
	}
})