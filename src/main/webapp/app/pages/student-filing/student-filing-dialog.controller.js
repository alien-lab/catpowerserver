/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('studentFilingDialogController',['$scope','$uibModalInstance','Learner',function ($scope,$uibModalInstance,Learner) {
        var vm = this;


        vm.clear = clear;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }]);


})();
