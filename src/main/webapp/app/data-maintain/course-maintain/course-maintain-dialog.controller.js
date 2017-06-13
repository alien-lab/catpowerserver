/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.controller('courseMaintainDialogController',['$timeout', '$scope', '$uibModalInstance','dataMaintain','Course',function ($timeout, $scope, $uibModalInstance,dataMaintain,Course) {
        var vm = this;
        vm.course = dataMaintain;
        vm.clear = clear;
        vm.save = save;


        console.log(vm.courseatlases);
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function save () {
            vm.isSaving = true;
            if (vm.course.id !== null) {
                Course.update(vm.course, onSaveSuccess, onSaveError);
            } else {
                Course.save(vm.course, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:courseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }]);
})();
