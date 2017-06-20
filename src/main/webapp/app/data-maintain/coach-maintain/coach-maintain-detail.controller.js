/**
 * Created by asus on 2017/6/13.
 */
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.controller('coachMaintainDetailController',['$scope','$rootScope', '$stateParams', 'previousState', 'dataMaintain', 'Coach',function ($scope,$rootScope, $stateParams, previousState, dataMaintain,Coach) {
        var vm = this;
        vm.coach = dataMaintain;
        console.log("////////////////////////////");
        console.log( vm.coach);
        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('catpowerserverApp:courseUpdate', function(event, result) {
            vm.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }]);


})();
