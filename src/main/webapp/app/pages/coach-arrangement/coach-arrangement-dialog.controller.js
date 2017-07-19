/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('arrangementController',['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CourseScheduling', 'Course', 'Coach', function ($timeout, $scope, $stateParams, $uibModalInstance, entity, CourseScheduling, Course, Coach) {
        var vm = this;

        vm.courseScheduling = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.courses = Course.query();
        vm.coaches = Coach.query();

        $timeout(function (){
            angular.element('.form-group:eq(0)>select').focus();
        });

        //上课状态
        $scope.statusList = [{
            id:'1',
            status:'未开始'
        },{
            id:'2',
            status:'进行中'
        },{
            id:'3',
            status:'已下课'
        }];
        $scope.getCoachStatus = function () {
            vm.courseScheduling.status = this.sta.status;
        };
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
