(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerChargeDeleteController',LearnerChargeDeleteController);

    LearnerChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'LearnerCharge'];

    function LearnerChargeDeleteController($uibModalInstance, entity, LearnerCharge) {
        var vm = this;

        vm.learnerCharge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LearnerCharge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
