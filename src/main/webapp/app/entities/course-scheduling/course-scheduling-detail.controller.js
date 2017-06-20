(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CourseSchedulingDetailController', CourseSchedulingDetailController);

    CourseSchedulingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CourseScheduling', 'Course', 'Coach'];

    function CourseSchedulingDetailController($scope, $rootScope, $stateParams, previousState, entity, CourseScheduling, Course, Coach) {
        var vm = this;

        vm.courseScheduling = entity;

        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:courseSchedulingUpdate', function(event, result) {

            console.log(result);
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
