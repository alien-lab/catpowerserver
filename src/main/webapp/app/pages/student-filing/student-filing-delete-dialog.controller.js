/**
 * Created by asus on 2017/6/18.
 */
(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('studentFilingDeleteController',studentFilingDeleteController);

    studentFilingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Learner'];

    function studentFilingDeleteController($uibModalInstance, entity, Learner) {
        var vm = this;

        vm.learner = entity;

        console.log(vm.learner);

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
