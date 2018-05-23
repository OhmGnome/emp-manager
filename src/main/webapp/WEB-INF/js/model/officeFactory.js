/**
 * An http request for office. The put method is necessarily declared
 * explicitly.
 * 
 *  
 * @memberof ModelModule
 * @name officeFactory
 * @ngdoc factory
 * @returns office
 */
modelModule.factory("officeFactory", function($resource) {
	return $resource('/office/', null, {
		'put' : {
			method : 'PUT'
		},
		'remove' : {
			url : '/office/id/:id',
			method : 'DELETE',
			params : {
				id : '@id'
			}
		}
	});
})