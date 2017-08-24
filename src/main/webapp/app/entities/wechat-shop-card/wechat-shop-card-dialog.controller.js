(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatShopCardDialogController', WechatShopCardDialogController);

    WechatShopCardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WechatShopCard', 'Course'];

    function WechatShopCardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WechatShopCard, Course) {
        var vm = this;

        vm.wechatShopCard = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.wechatShopCard.id !== null) {
                WechatShopCard.update(vm.wechatShopCard, onSaveSuccess, onSaveError);
            } else {
                WechatShopCard.save(vm.wechatShopCard, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:wechatShopCardUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.ctTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
