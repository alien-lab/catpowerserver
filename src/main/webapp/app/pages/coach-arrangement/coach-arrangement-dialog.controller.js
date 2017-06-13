/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('arrangementController',['$timeout', '$scope', '$stateParams', '$uibModalInstance','coachArrangementResource','dialogData', function ($timeout,$scope,$stateParams,$uibModalInstance,coachArrangementResource,dialogData) {
            var vm = this;

            vm.coach = dialogData;
            vm.clear = clear;

            $timeout(function (){
                angular.element('.form-group:eq(1)>input').focus();
            });

            function clear () {
                $uibModalInstance.dismiss('cancel');
            }
        function save () {
            vm.isSaving = true;
            if (vm.coach.coachId !== null) {
                Coach.update(vm.coach, onSaveSuccess, onSaveError);
            } else {
                Coach.save(vm.coach, onSaveSuccess, onSaveError);
            }
        }
        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:coachUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function onSaveError () {
            vm.isSaving = false;
        }
        //上课时间
        $scope.today = function() {
            $scope.startDate = new Date();
        };
        $scope.today();
        $scope.clear = function() {
            $scope.startDate = null;
        };
        $scope.startTime = function() {
            $scope.popup1.opened = true;
        };
        $scope.popup1 = {
            opened: false
        };
    }]);
})();
