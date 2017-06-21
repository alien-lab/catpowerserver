/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('addCourseController',["$timeout","$scope","$stateParams","$uibModalInstance","CourseScheduling","Course","Coach","addArrangement",
        function ($timeout, $scope, $stateParams, $uibModalInstance, CourseScheduling, Course, Coach,addArrangement) {
            var vm = this;

            vm.courseScheduling = addArrangement;
            vm.clear = clear;
            vm.datePickerOpenStatus = {};
            vm.openCalendar = openCalendar;
            vm.save = save;
            vm.courses = Course.query();
            vm.coaches = Coach.query();

            $timeout(function (){
                angular.element('.form-group:eq(1)>input').focus();
            });

            function clear () {
                $uibModalInstance.dismiss('cancel');
            }

            function save () {
                vm.isSaving = true;
                if (vm.courseScheduling.id !== null) {
                    CourseScheduling.update(vm.courseScheduling, onSaveSuccess, onSaveError);
                } else {
                    CourseScheduling.save(vm.courseScheduling, onSaveSuccess, onSaveError);
                }
            }

            function onSaveSuccess (result) {
                $scope.$emit('catpowerserverApp:courseSchedulingUpdate', result);
                $uibModalInstance.close(result);
                vm.isSaving = false;
            }

            function onSaveError () {
                vm.isSaving = false;
            }

            vm.datePickerOpenStatus.startTime = false;
            vm.datePickerOpenStatus.endTime = false;

            function openCalendar (date) {
                vm.datePickerOpenStatus[date] = true;
            }
    }]);
})();
