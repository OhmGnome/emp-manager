/**
 * A map module that contains all the model factorys. The model factorys are
 * RESTful factorys that call the back end controllers for persisted objects.
 * The $resource provider must be configured to not strip trailing slashes when
 * the backend controllers use '/' in their path.
 * 
 *  
 * @name ModelModule
 */

var modelModule = angular.module("ModelModule", [ 'ngResource' ])
	.config(function($resourceProvider) {
	$resourceProvider.defaults.stripTrailingSlashes = false;
});