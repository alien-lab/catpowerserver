/**
 * Created by asus on 2017/6/12.
 */
(function () {
    'use strict';
    angular
        .module('catpowerserverApp')
        .controller('courseMaintainDetailController',courseMaintainDetailController);
    courseMaintainDetailController.$inject =  ['$scope', '$rootScope', '$stateParams', 'previousState', 'dataMaintain', 'Course', 'CourseAtlas'];

    function courseMaintainDetailController($scope, $rootScope, $stateParams, previousState, dataMaintain, Course, CourseAtlas) {
        var vm = this;

        vm.course = dataMaintain;
        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('catpowerserverApp:courseUpdate', function(event, result) {
            vm.course = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }

})();
