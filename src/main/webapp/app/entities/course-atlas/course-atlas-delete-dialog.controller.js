(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CourseAtlasDeleteController',CourseAtlasDeleteController);

    CourseAtlasDeleteController.$inject = ['$uibModalInstance', 'entity', 'CourseAtlas'];

    function CourseAtlasDeleteController($uibModalInstance, entity, CourseAtlas) {
        var vm = this;

        vm.courseAtlas = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CourseAtlas.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
