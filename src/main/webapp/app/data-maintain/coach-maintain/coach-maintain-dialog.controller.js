/**
 * Created by asus on 2017/6/12.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('coachMaintainDialogController',['$timeout', '$scope', '$uibModalInstance','dataMaintain','Coach',function ($timeout, $scope, $uibModalInstance,dataMaintain,Coach) {
        var vm = this;
        vm.uploadurl="./api/image/upload";
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
            if (vm.coach.id !== null) {
                Coach.update(vm.coach, onSaveSuccess, onSaveError);
            } else {
                Coach.save(vm.coach, onSaveSuccess, onSaveError);
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
