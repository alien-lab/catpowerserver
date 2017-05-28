(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('BuyCourseDetailController', BuyCourseDetailController);

    BuyCourseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BuyCourse', 'Learner', 'Course', 'Coach'];

    function BuyCourseDetailController($scope, $rootScope, $stateParams, previousState, entity, BuyCourse, Learner, Course, Coach) {
        var vm = this;

        vm.buyCourse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:buyCourseUpdate', function(event, result) {
            vm.buyCourse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
