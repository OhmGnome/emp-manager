/**
 * A wrapper that contains the entire http factory for easier injection and better nomenclature
 * 
 *  
 * @memberof ModelModule 
 * @ngdoc factory
 * @name model
 */
modelModule.service("model", function(authService, userFactory, skillFactory, officeFactory, userSkillService, userOfficeService){
	this.auth = authService;
	this.user = userFactory;
	this.office = officeFactory;
	this.skill = skillFactory;
	this.userSkill = userSkillService;
	this.userOffice = userOfficeService;
})