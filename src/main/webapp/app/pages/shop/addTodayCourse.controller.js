/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('addCourseController',['$scope','$uibModalInstance',function ($scope,$uibModalInstance) {
        var vm = this;

        /*vm.coach = entity;*/
        vm.clear = clear;
        /*vm.save = save;*/

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }]);
})();
