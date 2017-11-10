(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('FriendyShopDialogController', FriendyShopDialogController);

    FriendyShopDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FriendyShop', 'QrInfo'];

    function FriendyShopDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FriendyShop, QrInfo) {
        var vm = this;

        vm.friendyShop = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.qrinfos = QrInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.friendyShop.id !== null) {
                FriendyShop.update(vm.friendyShop, onSaveSuccess, onSaveError);
            } else {
                FriendyShop.save(vm.friendyShop, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:friendyShopUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
