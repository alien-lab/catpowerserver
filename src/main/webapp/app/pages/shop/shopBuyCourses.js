(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('shopBuyCourseController', shopBuyCourseController);

    shopBuyCourseController.$inject = ['$scope','$state', 'BuyCourse', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','buyCoursesResource','$filter'];

    function shopBuyCourseController($scope,$state, BuyCourse, ParseLinks, AlertService, paginationConstants, pagingParams,buyCoursesResource,$filter) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 10;

        $scope.searchContent = null;

        loadAll();
        function loadAll () {
            BuyCourse.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onAllSuccess, onAllError);

        }
        function onAllSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            $scope.buyCourses = data;
            console.log(vm.buyCourses);
            vm.page = pagingParams.page;
        }
        function onAllError(error) {
            AlertService.error(error.data.message);
        }
        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                search: vm.currentSearch
            });
        }
       /* //模糊成查询
        $scope.searchContent = null;
        $scope.search = function () {
         BuyCourse.likeCourses({
                keyword:$scope.searchContent,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onSuccess, onError);

        };
        function onSuccess(data,headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            $scope.buyCourses = data;
            console.log($scope.buyCourses);
            vm.page = pagingParams.page;

        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
*/
        //根据时间查询
        $scope.open1 = function() {
            $scope.popup1.opened = true;
        };
        $scope.popup1 = {
            opened: false
        };
        $scope.open2 = function() {
            $scope.popup2.opened = true;
        };
        $scope.popup2 = {
            opened: false
        };

        /*$scope.startTime = null;
        $scope.endTime = null;
        $scope.currentTime = function () {
            if($scope.startTime != null && $scope.endTime != null){
                console.log($scope.startTime);
                console.log($scope.endTime);
                    BuyCourse.getCourseByTime({
                    butTime1:$filter('date')($scope.startTime,'yyyy-MM-dd'),
                    butTime2:$filter('date')($scope.endTime,'yyyy-MM-dd'),
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage
                },function (data,headers) {
                    vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;
                    $scope.buyCourses = data;
                    console.log(vm.buyCourses);
                    vm.page = pagingParams.page;
                });
            }
        };*/

    }
})();
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.factory('buyCoursesResource',['$resource',function ($resource) {
        var resourceUrl = 'api/buy-courses/like/keyword';
        return $resource(resourceUrl,{},{
            'likeCourses': { method: 'GET',isArray:true},
            'getCourseByTime':{url:'api/buy-courses/time',method: 'GET',isArray:true}
        });
    }]);

})();


