(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wechat-vipcard', {
            parent: 'entity',
            url: '/wechat-vipcard?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatVipcard.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-vipcard/wechat-vipcards.html',
                    controller: 'WechatVipcardController',
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
                    $translatePartialLoader.addPart('wechatVipcard');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wechat-vipcard-detail', {
            parent: 'wechat-vipcard',
            url: '/wechat-vipcard/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.wechatVipcard.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wechat-vipcard/wechat-vipcard-detail.html',
                    controller: 'WechatVipcardDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wechatVipcard');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WechatVipcard', function($stateParams, WechatVipcard) {
                    return WechatVipcard.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wechat-vipcard',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wechat-vipcard-detail.edit', {
            parent: 'wechat-vipcard-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-vipcard/wechat-vipcard-dialog.html',
                    controller: 'WechatVipcardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatVipcard', function(WechatVipcard) {
                            return WechatVipcard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-vipcard.new', {
            parent: 'wechat-vipcard',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-vipcard/wechat-vipcard-dialog.html',
                    controller: 'WechatVipcardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                openid: null,
                                cardJson: null,
                                getTime: null,
                                activeTime: null,
                                cardId: null,
                                cardCode: null,
                                cardStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wechat-vipcard', null, { reload: 'wechat-vipcard' });
                }, function() {
                    $state.go('wechat-vipcard');
                });
            }]
        })
        .state('wechat-vipcard.edit', {
            parent: 'wechat-vipcard',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-vipcard/wechat-vipcard-dialog.html',
                    controller: 'WechatVipcardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WechatVipcard', function(WechatVipcard) {
                            return WechatVipcard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-vipcard', null, { reload: 'wechat-vipcard' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wechat-vipcard.delete', {
            parent: 'wechat-vipcard',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wechat-vipcard/wechat-vipcard-delete-dialog.html',
                    controller: 'WechatVipcardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WechatVipcard', function(WechatVipcard) {
                            return WechatVipcard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wechat-vipcard', null, { reload: 'wechat-vipcard' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
