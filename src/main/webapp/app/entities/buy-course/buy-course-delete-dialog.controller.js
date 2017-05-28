(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('BuyCourseDeleteController',BuyCourseDeleteController);

    BuyCourseDeleteController.$inject = ['$uibModalInstance', 'entity', 'BuyCourse'];

    function BuyCourseDeleteController($uibModalInstance, entity, BuyCourse) {
        var vm = this;

        vm.buyCourse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BuyCourse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
