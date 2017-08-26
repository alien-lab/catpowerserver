(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wechat-shop-card', {
            parent: 'entity',
            url: '/wechat-shop-card?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatShopCard.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-shop-card/wechat-shop-cards.html',
                    controller: 'WechatShopCardController',
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
                    $translatePartialLoader.addPart('wechatShopCard');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wechat-shop-card-detail', {
            parent: 'wechat-shop-card',
            url: '/wechat-shop-card/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatShopCard.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-shop-card/wechat-shop-card-detail.html',
                    controller: 'WechatShopCardDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wechatShopCard');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WechatShopCard', function($stateParams, WechatShopCard) {
                    return WechatShopCard.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wechat-shop-card',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wechat-shop-card-detail.edit', {
            parent: 'wechat-shop-card-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card/wechat-shop-card-dialog.html',
                    controller: 'WechatShopCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatShopCard', function(WechatShopCard) {
                            return WechatShopCard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-shop-card.new', {
            parent: 'wechat-shop-card',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card/wechat-shop-card-dialog.html',
                    controller: 'WechatShopCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cardId: null,
                                title: null,
                                cardJson: null,
                                ctTime: null,
                                cardStatus: null,
                                cardRemainCount: null,
                                cardType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wechat-shop-card', null, { reload: 'wechat-shop-card' });
                }, function() {
                    $state.go('wechat-shop-card');
                });
            }]
        })
        .state('wechat-shop-card.edit', {
            parent: 'wechat-shop-card',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card/wechat-shop-card-dialog.html',
                    controller: 'WechatShopCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatShopCard', function(WechatShopCard) {
                            return WechatShopCard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-shop-card', null, { reload: 'wechat-shop-card' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-shop-card.delete', {
            parent: 'wechat-shop-card',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-shop-card/wechat-shop-card-delete-dialog.html',
                    controller: 'WechatShopCardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WechatShopCard', function(WechatShopCard) {
                            return WechatShopCard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-shop-card', null, { reload: 'wechat-shop-card' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
