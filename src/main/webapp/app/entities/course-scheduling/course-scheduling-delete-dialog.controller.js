(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CourseSchedulingDeleteController',CourseSchedulingDeleteController);

    CourseSchedulingDeleteController.$inject = ['$uibModalInstance', 'entity', 'CourseScheduling'];

    function CourseSchedulingDeleteController($uibModalInstance, entity, CourseScheduling) {
        var vm = this;

        vm.courseScheduling = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CourseScheduling.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
