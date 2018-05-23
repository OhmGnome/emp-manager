/**
 *  An http request for userOffice. The delete method is necessarily declared
 * explicitly because the content type-header was excluded by the angular
 * framework. The other way to do this is to write a rest controller in the
 * backend that takes the id of the object to delete in the url:
 * '/deleteUserOfficeMapping/{id}'.
 * 
 *  
 * @memberof ModelModule
 * @ngdoc service
 * @name userOfficeService 
 * 
 */
modelModule.service("userOfficeService", function($resource, $http) {
	/**
	 * @memberof userOfficeService
	 * @function getOfficesByUser
	 * @return List<Office>
	 * @param id
	 *            the id of the user to http request a list of offices for
	 */
	this.getOfficeByUser = function(id) {
		return $resource('/getOfficeByUser/:id').query({
			id : id
		});
	}

	/**
	 * @memberof userOfficeService
	 * @function addOfficesToUser
	 * @return List<Office>
	 * @param userOffices
	 *            A list of the relation objects of the join table that contains the id of
	 *            the user and the id of the office to save
	 */
	this.addOfficesToUser = function(userOffices) {
		return $resource('/addOfficesToUser/').save(userOffices);
	}

	/**
	 * @memberof userOfficeService
	 * @function deleteUserOffice
	 * @return Boolean
	 * @param userOffice
	 *            the relation object of the join table that contains the id of
	 *            the user and the id of the office to delete
	 */
	this.deleteUserOffice = function(userOffice) {
		var config = {
			method : "DELETE",
			url : '/deleteUserOfficeMapping/',
			data : userOffice,
			headers : {
				"Content-Type" : "application/json;charset=utf-8"
			}
		};
		$http(config);
	}
})