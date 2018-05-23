/**
 * An http request for userSkill. The delete method is necessarily declared
 * explicitly because the content type-header was excluded by the angular
 * framework. The other way to do this is to write a rest controller in the
 * backend that takes the id of the object to delete in the url:
 * '/deleteUserOfficeMapping/{id}'.
 * 
 *  
 * @memberof ModelModule
 * @ngdoc service
 * @name userSkillService
 */
modelModule.service("userSkillService", function($resource, $http) {

	/**
	 * @memberof userSkillService
	 * @function getSkillsByUser
	 * @return List<Skill>
	 * @param id
	 *            the id of the user to http request a list of skills for
	 */
	this.getSkillByUser = function(userId) {
		return $resource('/getSkillByUser/:id').query({
			id : userId
		});
	}

	/**
	 * @memberof userSkillService
	 * @function getOrderedUserSkills
	 * @return List<Skill>
	 * @param id
	 *            the id of the user to http request a list of skills for
	 */
	this.getOrderedUserSkills = function(userId) {
		return $resource('/getOrderedUserSkills/:id').query({
			id : userId
		});
	}

	/**
	 * @memberof userSkillService
	 * @function addSkillsToUser
	 * @return List<Skill>
	 * @param userSkills
	 *            A list of the relation objects of the join table that contains the id of
	 *            the user and the id of the skill to save
	 */
	this.addSkillsToUser = function(userSkills) {
		return $resource('/addSkillsToUser/').save(userSkills);
	}

	/**
	 * @memberof userSkillService
	 * @function deleteUserSkill
	 * @return Boolean
	 * @param userSkill
	 *            the relation object of the join table that contains the id of
	 *            the user and the id of the skill to delete
	 */
	this.deleteUserSkill = function(userSkill) {
		var config = {
			method : "DELETE",
			url : '/deleteUserSkill/',
			data : userSkill,
			headers : {
				"Content-Type" : "application/json;charset=utf-8"
			}
		};
		$http(config);
	}
})