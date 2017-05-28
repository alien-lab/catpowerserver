(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerChargeDetailController', LearnerChargeDetailController);

    LearnerChargeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LearnerCharge', 'Learner', 'Course', 'Coach', 'CourseScheduling'];

    function LearnerChargeDetailController($scope, $rootScope, $stateParams, previousState, entity, LearnerCharge, Learner, Course, Coach, CourseScheduling) {
        var vm = this;

        vm.learnerCharge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:learnerChargeUpdate', function(event, result) {
            vm.learnerCharge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
