(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('QrScanLogDetailController', QrScanLogDetailController);

    QrScanLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QrScanLog'];

    function QrScanLogDetailController($scope, $rootScope, $stateParams, previousState, entity, QrScanLog) {
        var vm = this;

        vm.qrScanLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:qrScanLogUpdate', function(event, result) {
            vm.qrScanLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
