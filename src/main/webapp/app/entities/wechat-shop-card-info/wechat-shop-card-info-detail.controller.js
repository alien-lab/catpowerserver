(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatShopCardInfoDetailController', WechatShopCardInfoDetailController);

    WechatShopCardInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WechatShopCardInfo', 'WechatShopCard'];

    function WechatShopCardInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, WechatShopCardInfo, WechatShopCard) {
        var vm = this;

        vm.wechatShopCardInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:wechatShopCardInfoUpdate', function(event, result) {
            vm.wechatShopCardInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
