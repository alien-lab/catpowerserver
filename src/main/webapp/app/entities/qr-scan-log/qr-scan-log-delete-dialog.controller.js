(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('QrScanLogDeleteController',QrScanLogDeleteController);

    QrScanLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'QrScanLog'];

    function QrScanLogDeleteController($uibModalInstance, entity, QrScanLog) {
        var vm = this;

        vm.qrScanLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QrScanLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
