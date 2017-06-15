(function() {
    'use strict';

    var app = angular.module('catpowerserverApp');
    app.controller('courseMaintainDeleteController',['$scope','$uibModalInstance', 'dataMaintain', 'Course',function ($scope,$uibModalInstance,dataMaintain,Course) {
        var vm = this;
        vm.course = dataMaintain;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Course.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }]);

})();
