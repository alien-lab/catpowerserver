(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('scheController', scheController);

    scheController.$inject = ['$scope','$state', 'CourseScheduling', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','courseScheService'];

    function scheController($scope,$state, CourseScheduling, ParseLinks, AlertService, paginationConstants, pagingParams,courseScheService) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        //vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.itemsPerPage = 15;

        loadAll();

        function loadAll () {
            CourseScheduling.query({
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
                $scope.courseSchedulings = data;
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
            courseScheService.loadLikeSches($scope.searchContent,function (data) {
                $scope.courseSchedulings = data;
            });
        };
    }
})();