(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wechat-shop-card-info', {
            parent: 'entity',
            url: '/wechat-shop-card-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatShopCardInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-shop-card-info/wechat-shop-card-infos.html',
                    controller: 'WechatShopCardInfoController',
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
                    $translatePartialLoader.addPart('wechatShopCardInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wechat-shop-card-info-detail', {
            parent: 'wechat-shop-card-info',
            url: '/wechat-shop-card-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatShopCardInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-shop-card-info/wechat-shop-card-info-detail.html',
                    controller: 'WechatShopCardInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wechatShopCardInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WechatShopCardInfo', function($stateParams, WechatShopCardInfo) {
                    return WechatShopCardInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wechat-shop-card-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wechat-shop-card-info-detail.edit', {
            parent: 'wechat-shop-card-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card-info/wechat-shop-card-info-dialog.html',
                    controller: 'WechatShopCardInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatShopCardInfo', function(WechatShopCardInfo) {
                            return WechatShopCardInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-shop-card-info.new', {
            parent: 'wechat-shop-card-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card-info/wechat-shop-card-info-dialog.html',
                    controller: 'WechatShopCardInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                openid: null,
                                cardId: null,
                                cardCode: null,
                                getTime: null,
                                rechargeTime: null,
                                outStr: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wechat-shop-card-info', null, { reload: 'wechat-shop-card-info' });
                }, function() {
                    $state.go('wechat-shop-card-info');
                });
            }]
        })
        .state('wechat-shop-card-info.edit', {
            parent: 'wechat-shop-card-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card-info/wechat-shop-card-info-dialog.html',
                    controller: 'WechatShopCardInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatShopCardInfo', function(WechatShopCardInfo) {
                            return WechatShopCardInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-shop-card-info', null, { reload: 'wechat-shop-card-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-shop-card-info.delete', {
            parent: 'wechat-shop-card-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card-info/wechat-shop-card-info-delete-dialog.html',
                    controller: 'WechatShopCardInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WechatShopCardInfo', function(WechatShopCardInfo) {
                            return WechatShopCardInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-shop-card-info', null, { reload: 'wechat-shop-card-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
