(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wechat-goods-list', {
            parent: 'entity',
            url: '/wechat-goods-list?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatGoodsList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-goods-list/wechat-goods-lists.html',
                    controller: 'WechatGoodsListController',
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
                    $translatePartialLoader.addPart('wechatGoodsList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wechat-goods-list-detail', {
            parent: 'wechat-goods-list',
            url: '/wechat-goods-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatGoodsList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-goods-list/wechat-goods-list-detail.html',
                    controller: 'WechatGoodsListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wechatGoodsList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WechatGoodsList', function($stateParams, WechatGoodsList) {
                    return WechatGoodsList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wechat-goods-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wechat-goods-list-detail.edit', {
            parent: 'wechat-goods-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-goods-list/wechat-goods-list-dialog.html',
                    controller: 'WechatGoodsListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatGoodsList', function(WechatGoodsList) {
                            return WechatGoodsList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-goods-list.new', {
            parent: 'wechat-goods-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-goods-list/wechat-goods-list-dialog.html',
                    controller: 'WechatGoodsListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                goodsName: null,
                                goodsMemo: null,
                                goodsPic: null,
                                buyButtonText: null,
                                limitCount: null,
                                goodsPrice: null,
                                sellStatus: null,
                                goodsField1: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wechat-goods-list', null, { reload: 'wechat-goods-list' });
                }, function() {
                    $state.go('wechat-goods-list');
                });
            }]
        })
        .state('wechat-goods-list.edit', {
            parent: 'wechat-goods-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-goods-list/wechat-goods-list-dialog.html',
                    controller: 'WechatGoodsListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatGoodsList', function(WechatGoodsList) {
                            return WechatGoodsList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-goods-list', null, { reload: 'wechat-goods-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-goods-list.delete', {
            parent: 'wechat-goods-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-goods-list/wechat-goods-list-delete-dialog.html',
                    controller: 'WechatGoodsListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WechatGoodsList', function(WechatGoodsList) {
                            return WechatGoodsList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-goods-list', null, { reload: 'wechat-goods-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
