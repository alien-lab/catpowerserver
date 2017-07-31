(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerChargeDialogController', LearnerChargeDialogController);

    LearnerChargeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LearnerCharge', 'Learner', 'Course', 'Coach', 'CourseScheduling','entityCourseScheduling'];

    function LearnerChargeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LearnerCharge, Learner, Course, Coach, CourseScheduling,entityCourseScheduling) {
        var vm = this;

        vm.learnerCharge = entity;
        vm.coursescheduling = entityCourseScheduling;
        console.log("****************************");
        console.log(vm.learnerCharge);
        console.log(vm.coursescheduling);

        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.coaches = Coach.query();
        vm.courseschedulings = CourseScheduling.query();

        //核销课程
        vm.learnerCharge.course = vm.coursescheduling.course;
        $scope.courseName = vm.coursescheduling.course.courseName;
        console.log(vm.learnerCharge.course);
        //核销的教练
        vm.learnerCharge.coach = vm.coursescheduling.coach;
        $scope.coachName = vm.coursescheduling.coach.coachName;
        console.log(vm.learnerCharge.coach);
        //核销排课id
        vm.learnerCharge.courseScheduling = vm.coursescheduling;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {

            vm.isSaving = true;
            LearnerCharge.save(vm.learnerCharge, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:learnerChargeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.chargeTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

