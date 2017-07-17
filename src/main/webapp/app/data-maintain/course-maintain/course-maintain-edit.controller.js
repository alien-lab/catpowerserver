/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.controller('courseMaintainEditController',['$timeout', '$scope', '$uibModalInstance','dataMaintain','Course','learnerCountService',function ($timeout, $scope, $uibModalInstance,dataMaintain,Course,learnerCountService) {
        var vm = this;
        vm.course = dataMaintain;
        console.log(vm.course.id);
        learnerCountService.loadlearnerCounts(vm.course.id,function (data) {
            $scope.learnerCount = data;
            console.log($scope.learnerCount.size);
        });
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
