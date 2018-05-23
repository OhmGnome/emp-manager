/**
 * An http request for skill. The put method is necessarily declared explicitly.
 * 
 *  
 * @memberof ModelModule
 * @ngdoc factory
 * @name skillFactory
 * @returns skill
 * 
 */
modelModule.factory("skillFactory", function($resource) {
	return $resource('/skill/', null, {
		'put' : {
			method : 'PUT'
		},
		'remove' : {
			url : '/skill/id/:id',
			method : 'DELETE',
			params : {
				id : '@id'
			}
		}
	});
})