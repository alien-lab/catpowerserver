(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CoachEvaluateDeleteController',CoachEvaluateDeleteController);

    CoachEvaluateDeleteController.$inject = ['$uibModalInstance', 'entity', 'CoachEvaluate'];

    function CoachEvaluateDeleteController($uibModalInstance, entity, CoachEvaluate) {
        var vm = this;

        vm.coachEvaluate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CoachEvaluate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
