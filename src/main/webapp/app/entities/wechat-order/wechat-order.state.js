(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wechat-order', {
            parent: 'entity',
            url: '/wechat-order?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-order/wechat-orders.html',
                    controller: 'WechatOrderController',
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
                    $translatePartialLoader.addPart('wechatOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wechat-order-detail', {
            parent: 'wechat-order',
            url: '/wechat-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-order/wechat-order-detail.html',
                    controller: 'WechatOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wechatOrder');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WechatOrder', function($stateParams, WechatOrder) {
                    return WechatOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wechat-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wechat-order-detail.edit', {
            parent: 'wechat-order-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-order/wechat-order-dialog.html',
                    controller: 'WechatOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatOrder', function(WechatOrder) {
                            return WechatOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-order.new', {
            parent: 'wechat-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-order/wechat-order-dialog.html',
                    controller: 'WechatOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNo: null,
                                orderTime: null,
                                payTime: null,
                                orderStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wechat-order', null, { reload: 'wechat-order' });
                }, function() {
                    $state.go('wechat-order');
                });
            }]
        })
        .state('wechat-order.edit', {
            parent: 'wechat-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-order/wechat-order-dialog.html',
                    controller: 'WechatOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatOrder', function(WechatOrder) {
                            return WechatOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-order', null, { reload: 'wechat-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-order.delete', {
            parent: 'wechat-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-order/wechat-order-delete-dialog.html',
                    controller: 'WechatOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WechatOrder', function(WechatOrder) {
                            return WechatOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-order', null, { reload: 'wechat-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
