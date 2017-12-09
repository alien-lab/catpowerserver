(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatOrderDetailController', WechatOrderDetailController);

    WechatOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WechatOrder', 'WechatUser', 'WechatGoodsList'];

    function WechatOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, WechatOrder, WechatUser, WechatGoodsList) {
        var vm = this;

        vm.wechatOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:wechatOrderUpdate', function(event, result) {
            vm.wechatOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
