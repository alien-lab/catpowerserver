/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('addCourseController',['$scope','$timeout','$stateParams','$uibModalInstance','coachArrangementService','addArrangement',
        function ($scope,$timeout,$stateParams,$uibModalInstance,coachArrangementService,addArrangement) {
        $scope.coachArrangements = coachArrangementService.loadCoachArrangement();
        console.log($scope.coachArrangements);

        var vm = this;
        vm.coach = addArrangement;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        function  clear() {
            $uibModalInstance.dismiss('cancel');
        }
        function save() {
            vm.isSaving = true;
            if(vm.coach.id !== null){
                coachArrangementService.update(vm.coach,onSaveSuccess,onSaveError);
            }else{
                Coach.save(vm.coach, onSaveSuccess, onSaveError);
            }
        }
        function onSaveSuccess(result){
            $scope.$emit('catpowerserverApp:coachArrangementService',result);
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
