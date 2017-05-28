(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CourseAtlasDialogController', CourseAtlasDialogController);

    CourseAtlasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CourseAtlas', 'Course'];

    function CourseAtlasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CourseAtlas, Course) {
        var vm = this;

        vm.courseAtlas = entity;
        vm.clear = clear;
        vm.save = save;
        vm.courses = Course.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.courseAtlas.id !== null) {
                CourseAtlas.update(vm.courseAtlas, onSaveSuccess, onSaveError);
            } else {
                CourseAtlas.save(vm.courseAtlas, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:courseAtlasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
