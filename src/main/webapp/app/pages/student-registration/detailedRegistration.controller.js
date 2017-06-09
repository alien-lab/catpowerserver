/**
 * Created by asus on 2017/6/8.
 */
/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('detailedRegistrationController',['$scope','$uibModalInstance',function ($scope,$uibModalInstance) {
        $scope.clear = function  () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);
})();
