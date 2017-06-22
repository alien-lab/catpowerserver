/**
 * Created by asus on 2017/6/13.
 */
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.controller('coachMaintainDetailController',['$scope','$http','$rootScope', '$stateParams', 'previousState', 'dataMaintain', 'Coach',function ($scope,$http,$rootScope, $stateParams, previousState, dataMaintain,Coach) {
        var vm = this;
        vm.coach = dataMaintain;
        console.log( vm.coach);
        vm.id = vm.coach.id;
        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('catpowerserverApp:courseUpdate', function(event, result) {
            vm.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);

        loadCoachInfo();
        function loadCoachInfo() {
            $http({
                url:"api/coaches/info/"+vm.id,
                method:"GET", isArray: true
            }).success(function (data) {
                $scope.coachDetail = data;
                console.log($scope.coachDetail);
            })
        }


    }]);

    //根据教练ID获取教练的信息
    /*app.factory("coachInfoResource",["$resource",function ($resource) {
        var resourceUrl =  'api/coaches/info/:id';
        return $resource(resourceUrl, {}, {
            'getCoachInfo': { method: 'GET', isArray: true}
        });
    }]);*/


})();
