/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('studentFilingStuqrController',['$timeout', '$scope','$stateParams','$uibModalInstance','Learner','entity','stuQrService','ticket',function ($timeout,$scope,$stateParams,$uibModalInstance,Learner,entity,stuQrService,ticket) {
        var vm = this;

        vm.learner = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function save () {
            vm.isSaving = true;
            if (vm.learner.id !== null) {
                Learner.update(vm.learner, onSaveSuccess, onSaveError);
            } else {
                Learner.save(vm.learner, onSaveSuccess, onSaveError);
            }
        }
        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:learnerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.registTime = false;
        vm.datePickerOpenStatus.firstTotime = false;
        vm.datePickerOpenStatus.firstBuyclass = false;
        vm.datePickerOpenStatus.recentlySignin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        //学员二维码绑定
        console.log(vm.learner.id);
        console.log(ticket);
        stuQrService.loadStuQr(vm.learner.id,function (data) {
            $scope.stuQr=data;
            $scope.stuqr = ticket + $scope.stuQr.qrTicker;
            console.log($scope.stuqr);
        });
    }]);


})();
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.factory('stuQrResource',['$resource',function ($resource) {
        var resourceUrl="api/learners/qr/learner";
        return $resource(resourceUrl,{}, {
            'getStuQr':{method:'GET'},
            'getLearner':{url:'api/learners/learnerName',method:'GET',isArray:true}
        });

    }]);
    app.service('stuQrService',['stuQrResource',function (stuQrResource) {
        //获取学员二维码
        this.loadStuQr = function (learner,callback) {
            stuQrResource.getStuQr({
                'learner':learner
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                if(callback){
                    console.log("stuQrResource.getStuQr()"+error);
                    callback(error,false);
                }
            });
        }
        //根据学员姓名查询
        this.loadLearnerByName = function (learnerName,callback) {
            stuQrResource.getLearner({
                'learnerName':learnerName
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                if(callback){
                    console.log("stuQrResource.getStuQr()"+error);
                    callback(error,false);
                }
            });
        }
    }])
})();
