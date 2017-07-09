(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerChargeDialogController', LearnerChargeDialogController);

    LearnerChargeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LearnerCharge', 'Learner', 'Course', 'Coach', 'CourseScheduling','shopopService'];

    function LearnerChargeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LearnerCharge, Learner, Course, Coach, CourseScheduling,shopopService) {
        var vm = this;

        vm.learnerCharge = entity;
        console.log(vm.learnerCharge);

        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.coaches = Coach.query();
        vm.courseschedulings = CourseScheduling.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.learnerCharge.id !== null) {
                LearnerCharge.update(vm.learnerCharge, onSaveSuccess, onSaveError);
            } else {
                LearnerCharge.save(vm.learnerCharge, onSaveSuccess, onSaveError);
            }
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

        //获取今日教练排课信息
        shopopService.loadCoachArrangement(function(data,flag){
            if(!flag){
                // alert(data);
            }
            $scope.coachArrangements = data;
            console.log($scope.coachArrangements);

        });
    }
})();

