(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerInfoDialogController', LearnerInfoDialogController);

    LearnerInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LearnerInfo', 'Learner'];

    function LearnerInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LearnerInfo, Learner) {
        var vm = this;

        vm.learnerInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.learnerInfo.id !== null) {
                LearnerInfo.update(vm.learnerInfo, onSaveSuccess, onSaveError);
            } else {
                LearnerInfo.save(vm.learnerInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:learnerInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
