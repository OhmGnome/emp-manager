/**
 * The profile controller leverages restful factorys to manipulate all of the persisted objects. This is a high traffic
 * implementation of rest. Get all is often called to refresh the data in the lists. It is done this way because we
 * assume that more than one person may be manipulating the data simultaneously. In this scenario we want the data
 * manipulators to have the most recent data. In the opposite scenario when we do not care about having the most recent
 * data we would simply assign the data returned by the rest call to a variable, then push that to the list. In this
 * case only the most recent changes by the individual user would appear to them.
 * 
 *  
 * @ngdoc controller
 * @memberof tracker
 * @name ProfileController
 */

app.controller("ProfileController", function($scope, $http, $log, model, CONSTANTS) {
	console.log('ProfileController');
	$scope.user = {};
	$scope.office = {};
	$scope.skill = {};

	/**
	 * gets all the offices
	 * 
	 * @memberof ProfileController
	 * @function getAllOffices
	 * @returns List<office>
	 */
	function getAllOffices() {
		$scope.allOffices = model.office.query();
		console.log('got all offices');
	}

	/**
	 * gets all the users
	 * 
	 * @memberof ProfileController
	 * @function getAllUsers
	 * @returns List<User>
	 */
	function getAllUsers() {
		$scope.allUsers = model.user.query();
		console.log('got all users');
	}

	getAllUsers();
	getAllOffices();

	/**
	 * gets all the skills of the $scope.user
	 * 
	 * @memberof ProfileController
	 * @function getSkillsByUser
	 * @returns List<Skill>
	 */
	function getSkillsByUser() {
		console.log('getSkillsByUser');
		$scope.userSkills = model.userSkill.getSkillByUser($scope.user.id);
	}

	/**
	 * gets all the offices of the $scope.office
	 * 
	 * @memberof ProfileController
	 * @function getOfficesByUser
	 * @returns List<Office>
	 */
	function getOfficesByUser() {
		console.log('getOfficesByUser');
		$scope.userOffices = model.userOffice.getOfficeByUser($scope.user.id);
		$scope.office = $scope.userOffices[0];
	}

	/**
	 * gets the skill history of the $scope.user
	 * 
	 * @memberof ProfileController
	 * @function getSkillsHistory
	 * @returns List<Skill>
	 */
	function getSkillsHistory() {
		console.log('success getSkillsHistory');
		$scope.skillsHistory = model.userSkill.getOrderedUserSkills($scope.user.id);
	}

	/**
	 * Leverages the amazon s3 SDK api to get a file from a bucket.
	 * 
	 * @memberof ProfileController
	 * @function getUserIconImage
	 * @returns base64 array
	 * @param icon
	 *            the name or partial url of the file
	 * 
	 */
	function getUserIconImage(userIcon) {
		if (userIcon) {
			console.log('getUserIconImage');

			var bucket = new AWS.S3({
				params : {
					Bucket : ''
				}
			});

			var params = {
				Key : 'filesP/' + userIcon
			};

			bucket.getObject(params, function(err, data) {
				console.log(err);
				if (data) {
					// encode data to base 64 url
					var blob = new Blob([ data.Body ], {
						'type' : 'image/png'
					});
					var fr = new FileReader();
					fr.onload = function() {
						// this variable holds your base64 image data URI (string)
						// use readAsBinary() or readAsBinaryString() below to obtain other data types
						$scope.userIconImage = fr.result;
						$scope.$applyAsync();
					};
					fr.readAsDataURL(blob);
					console.log('success getUserIconImage');
				} else {
					$scope.userIconImage = [];
					$scope.$applyAsync();
				}
			})
		} else {
			$scope.userIconImage = [];
			$scope.$applyAsync();
		}
	}

	function addressConcatenator(obj) {
		var address = obj.address2 ? obj.address1 + ' ' + obj.address2 : obj.address1;
		return address;
	}

	/**
	 * Sets $scope.user to the user selected in the all users list then calls on other functions to get that users'
	 * skills, skills history, and offices.
	 * 
	 * @memberof ProfileController
	 * @function editUser
	 * @Param selectedUser
	 *            a user from the allUsers list
	 */
	$scope.editUser = function(selectedUser) {
		console.log('select employee');
		$scope.user = selectedUser;
		$scope.pristineUser = angular.copy($scope.user);
		$scope.editUserIcon = $scope.user.icon;
		getSkillsByUser();
		getOfficesByUser();
		getSkillsHistory();
		getUserIconImage($scope.user.icon);
		$scope.userKeys = Reflect.ownKeys($scope.user);
	};
	
	$scope.isItAString = function (obj){
		if (obj === null){
			return true;
		}
		var stringCheck = typeof obj === "string";
		return stringCheck;
	}

	/**
	 * Assign the office selected in the all offices list to the $scope.office so it can be edited in the form. There
	 * can only be a single office selected from the list.
	 * 
	 * @memberof ProfileController
	 * @function editSkill
	 * @param Office
	 *            selectedOffice
	 */
	$scope.editOffice = function(selectedOffice) {
		console.log('edit Office()');
		$scope.office = selectedOffice;
		$scope.officeKeys = Reflect.ownKeys($scope.office);
	}
})