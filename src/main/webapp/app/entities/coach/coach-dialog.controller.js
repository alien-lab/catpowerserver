(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CoachDialogController', CoachDialogController);

    CoachDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Coach'];

    function CoachDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Coach) {
        var vm = this;

        vm.coach = entity;
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


    }
})();
