(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('FriendyShopDeleteController',FriendyShopDeleteController);

    FriendyShopDeleteController.$inject = ['$uibModalInstance', 'entity', 'FriendyShop'];

    function FriendyShopDeleteController($uibModalInstance, entity, FriendyShop) {
        var vm = this;

        vm.friendyShop = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FriendyShop.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
