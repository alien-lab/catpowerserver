/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('studentFilingController',['$scope','Learner','AlertService','pagingParams','ParseLinks','$state','stuQrService',function ($scope,Learner,AlertService,pagingParams,ParseLinks,$state,stuQrService) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 15;
        /**
         *获取学员信息
         */
        loadAll();
        function loadAll () {
            Learner.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data,headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                $scope.learners = data;
                console.log($scope.learners)
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch

            });
        }

        //根据学员姓名查询
        $scope.learnerName = null;
        $scope.getLeatnerByName = function () {
            stuQrService.loadLearnerByName($scope.learnerName,function (data) {
                $scope.learners=data;
                console.log($scope.learners);
            });
        };

    }]);
})();

