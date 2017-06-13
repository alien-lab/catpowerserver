/**
 * Created by asus on 2017/6/8.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('registrationController',['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'coursesService',function ($scope, $rootScope, $stateParams, previousState, entity, coursesService) {
        var vm = this;
        vm.courses = entity;
        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('catpowerserverApp:coursesUpdate', function(event, result) {
            vm.courses = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }]);
})();
