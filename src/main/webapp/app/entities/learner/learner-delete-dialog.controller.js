(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerDeleteController',LearnerDeleteController);

    LearnerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Learner'];

    function LearnerDeleteController($uibModalInstance, entity, Learner) {
        var vm = this;

        vm.learner = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Learner.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
