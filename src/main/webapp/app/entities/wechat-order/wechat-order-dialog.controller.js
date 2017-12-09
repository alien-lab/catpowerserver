(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatOrderDialogController', WechatOrderDialogController);

    WechatOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WechatOrder', 'WechatUser', 'WechatGoodsList'];

    function WechatOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WechatOrder, WechatUser, WechatGoodsList) {
        var vm = this;

        vm.wechatOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wechatusers = WechatUser.query();
        vm.wechatgoodslists = WechatGoodsList.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wechatOrder.id !== null) {
                WechatOrder.update(vm.wechatOrder, onSaveSuccess, onSaveError);
            } else {
                WechatOrder.save(vm.wechatOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:wechatOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.orderTime = false;
        vm.datePickerOpenStatus.payTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
