(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('FriendyShopController', FriendyShopController);

    FriendyShopController.$inject = ['$state', 'FriendyShop', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function FriendyShopController($state, FriendyShop, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;
        vm.qrvisible=false;
        vm.qrticket="";

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            FriendyShop.query({
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
                vm.friendyShops = data;
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

        vm.showqr=function(fshop){
            var id=fshop.id;
            FriendyShop.qr({id:id},function(result){
                console.log("qr",result);
                vm.qrticket=result.qrTicker;
                vm.qrvisible=true;
            },function(error){})
        }

        vm.hideqr=function(){
            vm.qrvisible=false;
        }
    }
})();
