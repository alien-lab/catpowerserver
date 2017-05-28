(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('BuyCourseDialogController', BuyCourseDialogController);

    BuyCourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BuyCourse', 'Learner', 'Course', 'Coach'];

    function BuyCourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BuyCourse, Learner, Course, Coach) {
        var vm = this;

        vm.buyCourse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.coaches = Coach.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.buyCourse.id !== null) {
                BuyCourse.update(vm.buyCourse, onSaveSuccess, onSaveError);
            } else {
                BuyCourse.save(vm.buyCourse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:buyCourseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.buyTime = false;
        vm.datePickerOpenStatus.operateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
