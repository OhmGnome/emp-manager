/**
 * 
 * An http request for user. The put method is necessarily declared explicitly.
 * 
 *  
 * @memberof ModelModule
 * @ngdoc factory
 * @name userFactory
 * @returns user
 * 
 */
modelModule.factory("userFactory", function($resource) {
	return $resource('/user/', null, {
		'put' : {
			method : 'PUT'
		},
		'remove' : {
			url : '/user/id/:id',
			method : 'DELETE',
			params : {
				id : '@id'
			}
		}
	});
})