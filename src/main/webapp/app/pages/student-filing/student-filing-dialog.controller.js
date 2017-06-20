/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('studentFilingDialogController',['$timeout', '$scope','$stateParams','$uibModalInstance','Learner','entity',function ($timeout,$scope,$stateParams,$uibModalInstance,Learner,entity) {
        var vm = this;

        vm.learner = entity;
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
        function save () {
            vm.isSaving = true;
            if (vm.learner.id !== null) {
                Learner.update(vm.learner, onSaveSuccess, onSaveError);
            } else {
                Learner.save(vm.learner, onSaveSuccess, onSaveError);
            }
        }
        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:learnerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.registTime = false;
        vm.datePickerOpenStatus.firstTotime = false;
        vm.datePickerOpenStatus.firstBuyclass = false;
        vm.datePickerOpenStatus.recentlySignin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }]);


})();
