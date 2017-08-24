(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatShopCardInfoDialogController', WechatShopCardInfoDialogController);

    WechatShopCardInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WechatShopCardInfo', 'WechatShopCard'];

    function WechatShopCardInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WechatShopCardInfo, WechatShopCard) {
        var vm = this;

        vm.wechatShopCardInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wechatshopcards = WechatShopCard.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wechatShopCardInfo.id !== null) {
                WechatShopCardInfo.update(vm.wechatShopCardInfo, onSaveSuccess, onSaveError);
            } else {
                WechatShopCardInfo.save(vm.wechatShopCardInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:wechatShopCardInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.getTime = false;
        vm.datePickerOpenStatus.rechargeTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
