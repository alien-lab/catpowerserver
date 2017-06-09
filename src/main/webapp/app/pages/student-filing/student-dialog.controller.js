/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('studentDialogController',['$scope','studentsService','$uibModalInstance','$timeout','student',function ($scope,studentsService,$uibModalInstance,$timeout,student) {
        $scope.students = studentsService.loadStudents();
        console.log($scope.students);

        var vm = this;

        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }]);


})();
