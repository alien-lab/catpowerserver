/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('StudentFilingDetailController',['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Learner',function ($scope, $rootScope, $stateParams, previousState, entity, Learner) {
        var vm = this;

        vm.learner = entity;

        console.log(vm.learner);
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:learnerUpdate', function(event, result) {
            vm.learner = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }]);


})();

