/**
 * Created by asus on 2017/6/12.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('coachMaintainDialogController',['$timeout', '$scope', '$uibModalInstance','dataMaintain','coachMaintainService',function ($timeout, $scope, $uibModalInstance,dataMaintain,coachMaintainService) {
        var vm = this;
        vm.coach = dataMaintain;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function save () {
            vm.isSaving = true;
            if (vm.course.courseId !== null) {
                coachMaintainService.loadCoach(vm.coach,onSaveSuccess, onSaveError)
            } else {
                coachMaintainService.loadCoach(vm.coach, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:coachUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }]);
})();
