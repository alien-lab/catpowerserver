(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CoachEvaluateDialogController', CoachEvaluateDialogController);

    CoachEvaluateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CoachEvaluate', 'Learner', 'Course', 'Coach', 'LearnerCharge'];

    function CoachEvaluateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CoachEvaluate, Learner, Course, Coach, LearnerCharge) {
        var vm = this;

        vm.coachEvaluate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.coaches = Coach.query();
        vm.learnercharges = LearnerCharge.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.coachEvaluate.id !== null) {
                CoachEvaluate.update(vm.coachEvaluate, onSaveSuccess, onSaveError);
            } else {
                CoachEvaluate.save(vm.coachEvaluate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:coachEvaluateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
