app.directive('ulForm', function($rootScope) {
	return {
		templateUrl : 'js/directives/templates/ulForm.html',
		restrict : 'A',
		scope : {
			obj : '=',
			property : '='
		},
	}
});