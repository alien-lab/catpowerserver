/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('studentWithdrawalController',['$scope','$state', 'CourseScheduling', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','backCoursesResource',
        function ($scope,$state, CourseScheduling, ParseLinks, AlertService, paginationConstants, pagingParams,backCoursesResource) {

            console.log("****************************");

            var vm = this;

            vm.loadPage = loadPage;
            vm.predicate = pagingParams.predicate;
            vm.reverse = pagingParams.ascending;
            vm.transition = transition;
            //vm.itemsPerPage = paginationConstants.itemsPerPage;
            vm.itemsPerPage = 3;

            $scope.backCourseList = [];
            $scope.searchContent = '退课';
            console.log($scope.backCourseList);
            console.log($scope.searchContent);
            searchSch();
            function searchSch() {
                backCoursesResource.getBackCourses({
                    status:$scope.searchContent,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage
                }, onSuccess, onError);
                function onSuccess(data,headers) {
                    vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;
                    $scope.backCourseList = data;
                    console.log($scope.backCourseList);
                    vm.page = pagingParams.page;

                }
                function onError(error) {
                    AlertService.error(error.data.message);
                }

            };
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

    }]);
})();
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.factory('backCoursesResource',['$resource',function ($resource) {
        var resourceUrl = 'api/buy-courses/status';
        return $resource(resourceUrl,{},{
            'getBackCourses':{method: 'GET',isArray:true}
        });
    }]);

})();

