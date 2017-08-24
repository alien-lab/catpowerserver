(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatVipcardDeleteController',WechatVipcardDeleteController);

    WechatVipcardDeleteController.$inject = ['$uibModalInstance', 'entity', 'WechatVipcard'];

    function WechatVipcardDeleteController($uibModalInstance, entity, WechatVipcard) {
        var vm = this;

        vm.wechatVipcard = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WechatVipcard.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
