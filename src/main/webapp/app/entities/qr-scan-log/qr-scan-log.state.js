(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('qr-scan-log', {
            parent: 'entity',
            url: '/qr-scan-log?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.qrScanLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/qr-scan-log/qr-scan-logs.html',
                    controller: 'QrScanLogController',
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
                    $translatePartialLoader.addPart('qrScanLog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('qr-scan-log-detail', {
            parent: 'qr-scan-log',
            url: '/qr-scan-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.qrScanLog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/qr-scan-log/qr-scan-log-detail.html',
                    controller: 'QrScanLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('qrScanLog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'QrScanLog', function($stateParams, QrScanLog) {
                    return QrScanLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'qr-scan-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('qr-scan-log-detail.edit', {
            parent: 'qr-scan-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qr-scan-log/qr-scan-log-dialog.html',
                    controller: 'QrScanLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QrScanLog', function(QrScanLog) {
                            return QrScanLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qr-scan-log.new', {
            parent: 'qr-scan-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qr-scan-log/qr-scan-log-dialog.html',
                    controller: 'QrScanLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                scanTime: null,
                                openid: null,
                                qrkey: null,
                                reply: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('qr-scan-log', null, { reload: 'qr-scan-log' });
                }, function() {
                    $state.go('qr-scan-log');
                });
            }]
        })
        .state('qr-scan-log.edit', {
            parent: 'qr-scan-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qr-scan-log/qr-scan-log-dialog.html',
                    controller: 'QrScanLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QrScanLog', function(QrScanLog) {
                            return QrScanLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qr-scan-log', null, { reload: 'qr-scan-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qr-scan-log.delete', {
            parent: 'qr-scan-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qr-scan-log/qr-scan-log-delete-dialog.html',
                    controller: 'QrScanLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QrScanLog', function(QrScanLog) {
                            return QrScanLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qr-scan-log', null, { reload: 'qr-scan-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
