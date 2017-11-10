(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('QrScanLogDialogController', QrScanLogDialogController);

    QrScanLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QrScanLog'];

    function QrScanLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QrScanLog) {
        var vm = this;

        vm.qrScanLog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.qrScanLog.id !== null) {
                QrScanLog.update(vm.qrScanLog, onSaveSuccess, onSaveError);
            } else {
                QrScanLog.save(vm.qrScanLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:qrScanLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.scanTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
