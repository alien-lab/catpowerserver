/**
 * Created by asus on 2017/6/12.
 */
(function () {
    'use strict';
    angular
        .module('catpowerserverApp')
        .controller('courseMaintainDetailController',courseMaintainDetailController);
    courseMaintainDetailController.$inject =  ['$scope', '$rootScope', '$stateParams', 'previousState', 'dataMaintain', 'Course', 'CourseAtlas','courseService'];

    function courseMaintainDetailController($scope, $rootScope, $stateParams, previousState, dataMaintain, Course, CourseAtlas,courseService) {
        var vm = this;
        vm.course = dataMaintain;
        console.log(vm.course.id);
        courseService.loadCourseById(vm.course.id,function (data) {
            $scope.courseById = data;
            console.log($scope.courseById)
        });


        var unsubscribe = $rootScope.$on('catpowerserverApp:courseUpdate', function(event, result) {
            vm.course = result;
        });

        $scope.$on('$destroy', unsubscribe);

    }

})();
