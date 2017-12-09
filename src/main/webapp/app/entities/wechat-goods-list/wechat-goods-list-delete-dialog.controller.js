(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatGoodsListDeleteController',WechatGoodsListDeleteController);

    WechatGoodsListDeleteController.$inject = ['$uibModalInstance', 'entity', 'WechatGoodsList'];

    function WechatGoodsListDeleteController($uibModalInstance, entity, WechatGoodsList) {
        var vm = this;

        vm.wechatGoodsList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WechatGoodsList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
