(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatShopCardDeleteController',WechatShopCardDeleteController);

    WechatShopCardDeleteController.$inject = ['$uibModalInstance', 'entity', 'WechatShopCard'];

    function WechatShopCardDeleteController($uibModalInstance, entity, WechatShopCard) {
        var vm = this;

        vm.wechatShopCard = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WechatShopCard.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
