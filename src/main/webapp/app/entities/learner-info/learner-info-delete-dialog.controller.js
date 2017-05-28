(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerInfoDeleteController',LearnerInfoDeleteController);

    LearnerInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'LearnerInfo'];

    function LearnerInfoDeleteController($uibModalInstance, entity, LearnerInfo) {
        var vm = this;

        vm.learnerInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LearnerInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
