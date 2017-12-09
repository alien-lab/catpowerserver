(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatOrderDeleteController',WechatOrderDeleteController);

    WechatOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'WechatOrder'];

    function WechatOrderDeleteController($uibModalInstance, entity, WechatOrder) {
        var vm = this;

        vm.wechatOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WechatOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
