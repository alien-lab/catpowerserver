(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CoachEvaluateDetailController', CoachEvaluateDetailController);

    CoachEvaluateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CoachEvaluate', 'Learner', 'Course', 'Coach', 'LearnerCharge'];

    function CoachEvaluateDetailController($scope, $rootScope, $stateParams, previousState, entity, CoachEvaluate, Learner, Course, Coach, LearnerCharge) {
        var vm = this;

        vm.coachEvaluate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:coachEvaluateUpdate', function(event, result) {
            vm.coachEvaluate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
