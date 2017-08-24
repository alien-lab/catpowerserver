(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatShopCardDetailController', WechatShopCardDetailController);

    WechatShopCardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WechatShopCard', 'Course'];

    function WechatShopCardDetailController($scope, $rootScope, $stateParams, previousState, entity, WechatShopCard, Course) {
        var vm = this;

        vm.wechatShopCard = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:wechatShopCardUpdate', function(event, result) {
            vm.wechatShopCard = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
