(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatShopCardInfoDeleteController',WechatShopCardInfoDeleteController);

    WechatShopCardInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'WechatShopCardInfo'];

    function WechatShopCardInfoDeleteController($uibModalInstance, entity, WechatShopCardInfo) {
        var vm = this;

        vm.wechatShopCardInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WechatShopCardInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
