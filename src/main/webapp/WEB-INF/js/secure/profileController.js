/**
 * The profile controller leverages restful factories and services to manipulate all of the persisted objects. This is a high traffic
 * implementation of rest. Get all is often called to refresh the data in the lists because we
 * assume that more than one person may be manipulating the data simultaneously. In this scenario we want the data
 * manipulators to have the most recent data. In the opposite scenario when we do not care about having the most recent
 * data we would simply assign the data returned by the rest call to a variable, then push that to the list. In this
 * case only the most recent changes by the individual user would appear to them.
 * 
 *  
 * @ngdoc controller
 * @memberof tracker
 * @name SecureProfileController
 */

app.controller("SecureProfileController", function($scope, $http, $log, $rootScope, model, CONSTANTS) {
	console.log('SecureProfileController');
	$scope.user = {};
	$scope.office = {};
	$scope.skill = {};
	$scope.userOffices = {};
	$scope.userSkills = {};
	$scope.isItVerified = false;
	$scope.pristineUser = null;
	$scope.pristineOffice = null;

	/**
	 * Self calling function cannot and should not be called elsewhere.
	 * Populates display data for the currently logged in user on page load.
	 * 
	 */
	(function getCurrentUser() {
		if (model.auth.username) {
			model.auth.getUserByUsername().$promise.then(function(data) {
				$scope.user = data;
				$scope.pristineUser = angular.copy($scope.user);
				getUserIconImage($scope.user.icon);
				$scope.loggedIn = true;
				getSkillsByUser();
				getOfficesByUser();
				getSkillsHistory();
				$scope.userKeys = Reflect.ownKeys($scope.user);
			})
		} else {
			$scope.user = {};
		}
	})()
	
	/**
	 * gets all the skills
	 * 
	 * @memberof ProfileController
	 * @function getAllSkills
	 * @returns List<skill>
	 */
	function getAllSkills() {
		$scope.allSkills = model.skill.query();
		console.log('got all skills');
	}

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

	getAllSkills();
	getAllOffices();
	getAllUsers();

	/**
	 * gets all the skills of the $scope.user
	 * 
	 * @memberof ProfileController
	 * @function getSkillsByUser
	 * @returns List<Skill>
	 */
	getSkillsByUser = function() {
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
	getOfficesByUser = function() {
		console.log('getOfficesByUser');
		model.userOffice.getOfficeByUser($scope.user.id).$promise.then(function(data){
		$scope.userOffices = data;
		$scope.office = $scope.userOffices[0];
		$scope.officeKeys = Reflect.ownKeys($scope.office);
		});
	}

	/**
	 * gets the skill history of the $scope.user
	 * 
	 * @memberof ProfileController
	 * @function getSkillsHistory
	 * @returns List<Skill>
	 */
	getSkillsHistory = function() {
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
	 * Resets all of $scope.user
	 * 
	 * @memberof ProfileController
	 * @function resetUser
	 */
	$scope.resetUser = function() {
		console.log('reset user');
		$scope.user = {};
		$scope.pristineUser = null;
		$scope.userSkills = {};
		$scope.userOffices = {};
	}

	/**
	 * Save or update the $scope.user then get all the users
	 * 
	 * @memberof ProfileController
	 * @function saveUser
	 */
	$scope.saveUser = function() {
		console.log('saveUser function');
		var userAddress = addressConcatenator($scope.user);

		// if post
		if ($scope.pristineUser === null) {
			getCoordinates();
		//if put and the user does not have coordinates
		}else if ($scope.user.lat === null || $scope.user.lng === null){
			getCoordinates();
		// if put and the user has coordinates but the address has changed
		} else if (userAddress !== addressConcatenator($scope.pristineUser)) {
			getCoordinates();
		} else {
			updateUser();
		}

		function getCoordinates() {
			if (userAddress) {
				$.getJSON(CONSTANTS.CONVERT_URL_PRE + userAddress + CONSTANTS.CONVERT_URL_SUF, null, function(data) {
					console.log(data); // important
					var coordinates = data.results[0].geometry.location;
					console.log(coordinates);
					$scope.user.lat = coordinates.lat;
					$scope.user.lng = coordinates.lng;
					updateUser();
				})
			}else{
				updateUser();
			}
		}

		function updateUser() {
			if ($scope.user.id) {
				model.user.put($scope.user).$promise.then(function(data) {
					console.log('save user success');
					$scope.messageUserSaved = 'Save Successful';
					$scope.user = data;
					if ($scope.editUserIcon !== $scope.user.icon) {
						getUserIconImage($scope.user.icon);
					}
					getAllUsers();
				}, function(error) {
					$scope.messageUserSaved = 'Failed to save';
					console.log(error);
				})
			} else {
				console.log($scope.user);
				model.user.save($scope.user).$promise.then(function(data, err) {
					console.log('save user success');
					$scope.user = data;
					getAllUsers();
					$scope.messageUserSaved = 'Save successful';
				}, function(error) {
					$scope.messageUserSaved = 'Failed to save';
					console.log(error);
				})
			}
		}
	};

	/**
	 * Leverages the amazon s3 SDK api to upload a file to a bucket.
	 * 
	 * @memberof ProfileController
	 * @function onSelectUserIcon
	 * @returns Uint8~ array
	 * @param element
	 *            the html element of the file picker
	 * 
	 */
	$scope.onSelectUserIcon = function(element) {
		console.log('onSelectUserIcon function');
		var files = element.files;

		var bucket = new AWS.S3({
			params : {
				Bucket : ''
			}
		});

		console.log('files.length ' + files.length);
		console.log(files[0]);
		// var host = 'http://.s3-us-west-1.amazonaws.com/filesP/';
		var message = '';
		for (var i = 0; i < files.length; i++) {
			var file = files[i];
			if (file) {
				var params = {
					Key : 'filesP/' + file.name,
					ContentType : file.type,
					Body : file
				};
				bucket.upload(params, function(err, data) {
					$scope.messageUpload = err ? 'Error: An error occured the file was not uploaded'
							: 'Success: the file was uploaded and the path was saved to the user';
					console.log($scope.messageUpload);
					console.log(err);
					$scope.user.icon = file.name;
					$scope.saveUser();
				});
			} else {
				$scope.messageUpload = 'Nothing to upload.';
			}
		}
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
//		$scope.verifiedPhoneNumber($scope.user.phone);
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
	 * returns boolean to tell whether a number is verified or not
	 * 
	 * @memberof ProfileController
	 * @function isVerified
	 * @Param phonenum
	 */
//	$scope.verifiedPhoneNumber = function(phonenum) {
//		if (phonenum != undefined || phonenum != null) {
//			$http.post('user/isItVerified', phonenum).success(function(data) {
//				$scope.isItVerified = data;
//			}).error(function() {
//				console.log("Phone not verified");
//			});
//		} else {
//			$scope.isItVerified = false;
//		}
//	}

	/**
	 * verify the user's phone number
	 * 
	 * @memberof ProfileController
	 * @function verify
	 * @Param phonenum
	 */
	$scope.verify = function(phonenum) {
		$http.post('user/verify', phonenum).success(function(data) {
			$scope.validationCode = data;
		}).error(function() {
			console.log("Phone not verified");
		});
	}

	/**
	 * delete the user from the database then get all the users
	 * 
	 * @memberof ProfileController
	 * @function deleteUser
	 * @Param selectedUser
	 *            a user from the allUsers list
	 */
	$scope.deleteUser = function(selectedUser) {
		console.log('deleteUser function');
		model.user.remove($scope.user);
		getAllUsers();
	};

	/**
	 * Saves the selected skills from the all skills list to the users' skills list if that skill is not already in the
	 * users' skills list. Updates the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveSkillsToUser
	 * @param List
	 *            <skill> selectedSkills
	 */
	$scope.saveSkillsToUser = function(selectedSkills) {
		console.log('skills function');
		var userSkills = [];
		var j;
		var i;
		var k;
		if ($scope.userSkills !== null & $scope.userSkills.length > 0) {
			for (i = selectedSkills.length - 1; i > -1; i--) {
				for (j = $scope.userSkills.length - 1; j > -1; j--) {
					if ($scope.userSkills[j].id === selectedSkills[i].id) {
						selectedSkills.splice(i, 1);
						break;
					}
				}
			}
		}
		var userSkills = {};
		userSkills.userId = $scope.user.id;
		userSkills.skillIds = [];
		for (k = 0; k < selectedSkills.length; k++) {
			userSkills.skillIds.push(selectedSkills[k].id);
		}
		console.log(userSkills);
		model.userSkill.addSkillsToUser(userSkills);
		setTimeout(function() {
			getSkillsByUser();
			getSkillsHistory();
		}, 1000);
	}

	/**
	 * Delete the selected skills from the list of users' skills and update the page with the most recent database
	 * information.
	 * 
	 * @memberof ProfileController
	 * @function deleteSkillsFromUser
	 * @param List
	 *            <Skill> selectedUserSkills
	 */
	$scope.deleteSkillsFromUser = function(selectedUserSkills) {
		angular.forEach(selectedUserSkills, function(value, key) {
			var userSkill = {};
			userSkill['userId'] = $scope.user.id;
			userSkill['skillId'] = value.id;
			model.userSkill.deleteUserSkill(userSkill);
		});
		setTimeout(function() {
			getSkillsByUser();
			getSkillsHistory();
		}, 1000);
	}

	/**
	 * Save or update $scope.skill to the database and update the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveSkill
	 */
	$scope.saveSkill = function() {
		console.log('saveSkill function');
		if ($scope.skill.id) {
			model.skill.put($scope.skill).$promise.then(function(){
					getAllSkills();
			});
		} else {
			model.skill.save($scope.skill).$promise.then(function(){
					getAllSkills();
			});
		}
		getAllSkills();
		$scope.tooMuchSkilltoEdit = false;
		$scope.resetSkill();
	}

	/**
	 * Save or update $scope.skill in the form then create a relation between that skill and the $scope.user and update
	 * the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveNewSkillToUser
	 */
	$scope.saveNewSkillToUser = function() {
		console.log('saveSkillToEmployee function');
		var i;
		$scope.tooMuchSkilltoEdit = false;
		var userSkills = {};
		userSkills.userId = $scope.user.id;
		userSkills.skillIds = [];
		if ($scope.skill.id) {
			$scope.skill = model.skill.put($scope.skill);
			userSkills.skillIds.push($scope.skill.id);
			for (i = 0; i < $scope.userSkills.length; i++) {
				if ($scope.skill.id === $scope.userSkills[i].id) {
					console.log("selected skill is in userSkills exiting");
					getSkillsByUser();
					getSkillsHistory();
					getAllSkills();
					$scope.resetSkill();
					return;
				}
			}
			model.userSkill.addSkillsToUser(userSkills);
		} else {
			model.skill.save($scope.skill).$promise.then(function(data) {
				userSkills.skillIds.push(data.id);
				model.userSkill.addSkillsToUser(userSkills);
				$scope.userSkills.push(data);
				$scope.allSkills.push(data);
			});
		}
		$scope.resetSkill();
		// the table wouldn't update even with 'promise & then' magic. so
		// later...
		setTimeout(function() {
			getSkillsHistory();
		}, 1000);
	}

	/**
	 * Assign the skill selected in the all skills list to the $scope.skill so it can be edited in the form. If there is
	 * more than one skill selected set a flag that warns the user of their incompetence.
	 * 
	 * @memberof ProfileController
	 * @function editSkill
	 * @param selectedSkills
	 *            the skills selected in the allSkills list to be edited.
	 */
	$scope.editSkill = function(selectedSkills) {
		console.log('edit skill()');
		if (selectedSkills.length > 1) {
			$scope.tooMuchSkilltoEdit = true;
		}
		$scope.skill = selectedSkills[0];
	}

	/**
	 * delete $scope.skill from the database and update the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveSkill
	 */
	$scope.deleteSkill = function(selectedSkills) {
		console.log('deleteSkill function');
		model.skill.remove(selectedSkills);
		getAllSkills();
		getSkillsByUser();
		getSkillsHistory();
	}

	/**
	 * Reset the current skill being edited so that angular looses its connection with the object.
	 * 
	 * @memberof ProfileController
	 * @function resetSkill
	 */
	$scope.resetSkill = function() {
		console.log('$scope.resetSkill()');
		$scope.tooMuchSkilltoEdit = false;
		$scope.skill = {};
	}

	/**
	 * Saves the selected office from the all offices list to the users' offices list if that office is not already in
	 * the users' offices list. The number of offices that a user may have is currently limited to one by the backend.
	 * Updates the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveOfficeToUser
	 * @param Office
	 *            selectedOffice
	 */
	$scope.saveOfficeToUser = function(selectedOffice) {
		console.log('saveOfficetoUser function');
		var i;
		for (i = 0; i < $scope.userOffices.length; i++) {
			if (selectedOffice.id === $scope.userOffices[i].id) {
				console.log("selected office is in userOffices exiting");
				return;
			}
		}
		var userOffices = {};
		userOffices.userId = $scope.user.id;
		userOffices.officeIds = [];
		userOffices.officeIds.push(selectedOffice.id);
		model.userOffice.addOfficesToUser(userOffices);
		setTimeout(function() {
			getOfficesByUser();
		}, 1000);
	}

	/**
	 * Delete the selected office from the list of users' offices and update the page with the most recent database
	 * information.
	 * 
	 * @memberof ProfileController
	 * @function deleteOfficesFromUser
	 * @param Office
	 *            selectedUserOffice
	 */
	$scope.deleteOfficeFromUser = function(selectedUserOffice) {
		var userOffice = {};
		userOffice['userId'] = $scope.user.id;
		userOffice['officeId'] = selectedUserOffice.id;
		model.userOffice.deleteUserOffice(userOffice);
		setTimeout(function() {
			getOfficesByUser();
		}, 1000);
	}

	/**
	 * Save or update $scope.office to the database and update the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveOffice
	 */
	$scope.saveOffice = function() {
		console.log('saveOffice function');
		var officeAddress = addressConcatenator($scope.office);

		// if post
		if ($scope.pristineOffice === null) {
			getCoordinates();
		} else if ($scope.office.lat === null || $scope.office.lng === null) {
			getCoordinates();
		} else if (officeAddress !== addressConcatenator($scope.pristineOffice)) {
			getCoordinates();
		} else {
			updateOffice();
		}

		function getCoordinates() {
			if (officeAddress) {
				$.getJSON(CONSTANTS.CONVERT_URL_PRE + officeAddress + CONSTANTS.CONVERT_URL_SUF, null, function(data) {
					console.log(data); // important
					var coordinates = data.results[0].geometry.location;
					console.log(coordinates);
					$scope.office.lat = coordinates.lat;
					$scope.office.lng = coordinates.lng;
					updateOffice();
				})
			}else{
				updateOffice();
			}
		}

		function updateOffice() {
			if ($scope.office.id) {
				model.office.put($scope.office).$promise.then(function(){
					getAllOffices();
				});
			} else {
				model.office.save($scope.office).$promise.then(function(){
					getAllOffices();
				});
			}
			$scope.office = {};
		}
	}

	/**
	 * Save or update $scope.office in the form then create a relation between that office and the $scope.user and
	 * update the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveNewOfficeToEmployee
	 */
	$scope.saveNewOfficeToUser = function() {
		console.log('saveNewOfficetoUser function');
		var i;
		var userOffices = {};
		userOffices.userId = $scope.user.id;
		userOffices.officeIds = [];
		var officeAddress = addressConcatenator($scope.office);

		// if post
		if ($scope.pristineOffice === null) {
			getCoordinates();
		} else if ($scope.office.lat === null || $scope.office.lng === null) {
			getCoordinates();
		} else if (officeAddress !== addressConcatenator($scope.pristineOffice)) {
			getCoordinates();
		} else {
			updateOffice();
		}

		function getCoordinates() {
			if (officeAddress) {
				$.getJSON(CONSTANTS.CONVERT_URL_PRE + officeAddress + CONSTANTS.CONVERT_URL_SUF, null, function(data) {
					console.log(data); // important
					var coordinates = data.results[0].geometry.location;
					console.log(coordinates);
					$scope.office.lat = coordinates.lat;
					$scope.office.lng = coordinates.lng;
					updateOffice();
				})
			}else{
				updateOffice();
			}
		}

		function updateOffice() {
			if ($scope.office.id) {
				$scope.office = model.office.put($scope.office);
				userOffices.officeIds.push($scope.office.id);
				getAllOffices();
				for (i = 0; i < $scope.userOffices.length; i++) {
					if ($scope.office.id === $scope.userOffices[i].id) {
						console.log("selected office is in userOffices exiting");
						getOfficesByUser();
						return;
					}
				}
				model.userOffice.addOfficesToUser(userOffices).$promise.then(function(data) {
					getOfficesByUser();
				});
			} else {
				model.office.save($scope.office).$promise.then(function(data) {
					userOffices.officeIds.push(data.id);
					model.userOffice.addOfficesToUser(userOffices);
					getOfficesByUser();
				});
			}
			getAllOffices();
		}
	};

	/**
	 * Reset $scope.office so that the current office being edited so that angular looses its connection with the
	 * object.
	 * 
	 * @memberof ProfileController
	 * @function resetOffice
	 */
	$scope.resetOffice = function() {
		console.log('$scope.resetOffice()');
		$scope.office = {};
		$scope.pristineOffice = null;
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

	/**
	 * delete $scope.office from the database and update the page with the most recent database information.
	 * 
	 * @memberof ProfileController
	 * @function saveSkill
	 */
	$scope.deleteOffice = function(selectedOffice) {
		console.log('deleteSkill function');
		model.office.remove(selectedOffice);
		getAllOffices();
		getOfficesByUser();
	}

})