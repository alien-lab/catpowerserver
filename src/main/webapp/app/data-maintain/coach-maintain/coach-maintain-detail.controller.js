/**
 * Created by asus on 2017/6/13.
 */
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.controller('coachMaintainDetailController',['$scope','$http','$rootScope', '$stateParams', 'previousState', 'dataMaintain','coachInfoService',function ($scope,$http,$rootScope, $stateParams, previousState, dataMaintain,coachInfoService) {
        var vm = this;
        vm.coach = dataMaintain;
        console.log( vm.coach);

        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('catpowerserverApp:courseUpdate', function(event, result) {
            vm.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);

        //获取教练的课程
        $scope.courseStatus = true;
        coachInfoService.loadCoachOtherInfo(vm.coach.id,function (data) {
            $scope.coachCourse = data;
            console.log($scope.coachCourse);
            angular.forEach($scope.coachCourse,function (item) {
                if(item.course_name != null){
                    $scope.courseStatus = false;
                    $scope.courseStatusY = true;
                }
            })
        });
        //根据教练ID获取教练的评价信息
        $scope.complainStatus = true;
        coachInfoService.loadCoachComment(vm.coach.id,function (data) {
            $scope.coachComments = data;
            angular.forEach($scope.coachComments,function (item) {
                if(item.complain != null){
                    $scope.complainStatus = false;
                    $scope.complainStatusY = true;
                }
            })
        });
        /*loadCoachInfo();
        function loadCoachInfo() {
            $http({
                url:"api/coaches/info/"+vm.coach.id,
                method:"GET", isArray: true
            }).success(function (data) {
                $scope.coachDetail = data;
                console.log($scope.coachDetail);
            })
        }*/


    }]);

    //根据教练ID获取教练的信息
    /*app.factory("coachInfoResource",["$resource",function ($resource) {
        var resourceUrl =  'api/coaches/info/:id';
        return $resource(resourceUrl, {}, {
            'getCoachInfo': { method: 'GET', isArray: true}
        });
    }]);*/


})();
