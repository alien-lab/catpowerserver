(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatVipcardDetailController', WechatVipcardDetailController);

    WechatVipcardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WechatVipcard'];

    function WechatVipcardDetailController($scope, $rootScope, $stateParams, previousState, entity, WechatVipcard) {
        var vm = this;

        vm.wechatVipcard = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:wechatVipcardUpdate', function(event, result) {
            vm.wechatVipcard = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
