(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CourseAtlasDetailController', CourseAtlasDetailController);

    CourseAtlasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CourseAtlas', 'Course'];

    function CourseAtlasDetailController($scope, $rootScope, $stateParams, previousState, entity, CourseAtlas, Course) {
        var vm = this;

        vm.courseAtlas = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:courseAtlasUpdate', function(event, result) {
            vm.courseAtlas = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
