(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friendy-shop', {
            parent: 'entity',
            url: '/friendy-shop?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.friendyShop.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendy-shop/friendy-shops.html',
                    controller: 'FriendyShopController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendyShop');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('friendy-shop-detail', {
            parent: 'friendy-shop',
            url: '/friendy-shop/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.friendyShop.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendy-shop/friendy-shop-detail.html',
                    controller: 'FriendyShopDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendyShop');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FriendyShop', function($stateParams, FriendyShop) {
                    return FriendyShop.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'friendy-shop',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('friendy-shop-detail.edit', {
            parent: 'friendy-shop-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendy-shop/friendy-shop-dialog.html',
                    controller: 'FriendyShopDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FriendyShop', function(FriendyShop) {
                            return FriendyShop.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendy-shop.new', {
            parent: 'friendy-shop',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendy-shop/friendy-shop-dialog.html',
                    controller: 'FriendyShopDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                shopName: null,
                                shopDesc: null,
                                shopPosition: null,
                                createTime: null,
                                endTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('friendy-shop', null, { reload: 'friendy-shop' });
                }, function() {
                    $state.go('friendy-shop');
                });
            }]
        })
        .state('friendy-shop.edit', {
            parent: 'friendy-shop',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendy-shop/friendy-shop-dialog.html',
                    controller: 'FriendyShopDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FriendyShop', function(FriendyShop) {
                            return FriendyShop.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendy-shop', null, { reload: 'friendy-shop' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendy-shop.delete', {
            parent: 'friendy-shop',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendy-shop/friendy-shop-delete-dialog.html',
                    controller: 'FriendyShopDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FriendyShop', function(FriendyShop) {
                            return FriendyShop.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendy-shop', null, { reload: 'friendy-shop' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
