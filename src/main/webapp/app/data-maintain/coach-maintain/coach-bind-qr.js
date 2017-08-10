/**
 * Created by asus on 2017/6/9.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('coachBindQrController',['$timeout', '$scope','$stateParams','$uibModalInstance','Learner','entity','coachQrService','ticket',function ($timeout,$scope,$stateParams,$uibModalInstance,Learner,entity,coachQrService,ticket) {
        var vm = this;

        vm.coach = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        vm.datePickerOpenStatus.registTime = false;
        vm.datePickerOpenStatus.firstTotime = false;
        vm.datePickerOpenStatus.firstBuyclass = false;
        vm.datePickerOpenStatus.recentlySignin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        //学员二维码绑定
        console.log(vm.coach.id);
        console.log(ticket);
        coachQrService.loadCoachQr(vm.coach.id,function (data) {
            $scope.coachQr=data;
            $scope.coachBindQr = ticket + $scope.coachQr.qrTicker;
            console.log($scope.coachQr);
        });
    }]);


})();
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.factory('coachQrResource',['$resource',function ($resource) {
        var resourceUrl="api/coaches/qr/coach";
        return $resource(resourceUrl,{}, {
            'getCoachBindQr':{method:'GET'}
        });

    }]);
    app.service('coachQrService',['coachQrResource',function (coachQrResource) {
        //获取教练二维码
        this.loadCoachQr = function (coach,callback) {
            coachQrResource.getCoachBindQr({
                'coach':coach
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                if(callback){
                    console.log("coachQrResource.getCoachBindQr()"+error);
                    callback(error,false);
                }
            });
        }
    }])
})();
