(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatVipcardDialogController', WechatVipcardDialogController);

    WechatVipcardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WechatVipcard'];

    function WechatVipcardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WechatVipcard) {
        var vm = this;

        vm.wechatVipcard = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wechatVipcard.id !== null) {
                WechatVipcard.update(vm.wechatVipcard, onSaveSuccess, onSaveError);
            } else {
                WechatVipcard.save(vm.wechatVipcard, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:wechatVipcardUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.getTime = false;
        vm.datePickerOpenStatus.activeTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
