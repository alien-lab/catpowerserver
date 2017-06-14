/**
 * Created by asus on 2017/6/13.
 */
(function() {
    'use strict';

    var app = angular.module('catpowerserverApp');
    app.controller('coachMaintainDeleteController',['$scope','$uibModalInstance', 'dataMaintain', 'Coach',function ($scope,$uibModalInstance,dataMaintain,Coach) {
        var vm = this;
        vm.coach = dataMaintain;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Coach.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }]);

})();
