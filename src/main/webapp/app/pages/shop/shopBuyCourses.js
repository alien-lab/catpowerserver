(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('shopBuyCourseController', shopBuyCourseController);

    shopBuyCourseController.$inject = ['$scope','$state', 'BuyCourse', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','buyCoursesServer'];

    function shopBuyCourseController($scope,$state, BuyCourse, ParseLinks, AlertService, paginationConstants, pagingParams,buyCoursesServer) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 15;

        loadAll();

        function loadAll () {
            BuyCourse.query({
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
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                $scope.buyCourses = data;

                console.log(vm.buyCourses);
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

        //模糊成查询
        $scope.searchContent = null;
        $scope.search = function () {
            buyCoursesServer.loadLikeCourses($scope.searchContent,function (data) {
                $scope.buyCourses = data;
                console.log($scope.buyCourses);
            });
        };

        //
        //获取当前日期
        var now = new Date();
        var month = now.getMonth()+1;
        var nowDate = now.getFullYear()+'-'+month+'-'+now.getDate();
        $scope.nowDate = nowDate;
        //时段
        $scope.today = function() {
            $scope.startDate = new Date();
            $scope.endDate = new Date();
            $scope.startArrangementCourseDate = new Date();
            $scope.endArrangementCourseDate = new Date();
        };
        $scope.today();
        $scope.clear = function() {
            $scope.startDate = null;
            $scope.endDate = null;
            $scope.startArrangementCourseDate = null;
            $scope.endArrangementCourseDate = null;
        };
        $scope.startTime = function() {
            $scope.popup1.opened = true;
        };
        $scope.endTime = function() {
            $scope.popup2.opened = true;
        };
        $scope.startArrangementCourse = function() {
            $scope.popupStartArrangementCourse.opened = true;
        };
        $scope.endArrangementCourse = function() {
            $scope.popupEndArrangementCourse.opened = true;
        };
        $scope.popup1 = {
            opened: false
        };
        $scope.popup2 = {
            opened: false
        };
        $scope.popupStartArrangementCourse = {
            opened: false
        };
        $scope.popupEndArrangementCourse = {
            opened: false
        };


    }
})();
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.factory('buyCoursesResource',['$resource',function ($resource) {
        var resourceUrl = 'api/buy-courses/like/keyword';
        return $resource(resourceUrl,{},{
            'likeCourses': { method: 'GET',isArray:true}
        });
    }]);
    app.service('buyCoursesServer',['buyCoursesResource',function (buyCoursesResource) {
        this.loadLikeCourses = function (keyword,callback) {
            buyCoursesResource.likeCourses({
                "keyword":keyword
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("buyCoursesResource.likeCourses()"+error);
                if(callback){
                    callback(error,false);
                }
            });
        }
    }]);
})();


