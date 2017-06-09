/**
 * Created by asus on 2017/6/8.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('registrationController',['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Course',function ($timeout, $scope, $stateParams, $uibModalInstance, entity, Course) {
        var vm = this;

        vm.course = entity;
        vm.clear = clear;
        vm.save = save;
        vm.courseatlases = CourseAtlas.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.clear = function  () {
            $uibModalInstance.dismiss('cancel');
        };

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
