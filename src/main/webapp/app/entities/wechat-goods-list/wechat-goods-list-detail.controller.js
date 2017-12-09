(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatGoodsListDetailController', WechatGoodsListDetailController);

    WechatGoodsListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WechatGoodsList', 'WechatShopCard', 'Course'];

    function WechatGoodsListDetailController($scope, $rootScope, $stateParams, previousState, entity, WechatGoodsList, WechatShopCard, Course) {
        var vm = this;

        vm.wechatGoodsList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:wechatGoodsListUpdate', function(event, result) {
            vm.wechatGoodsList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
