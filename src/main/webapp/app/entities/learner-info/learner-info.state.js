(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('learner-info', {
            parent: 'entity',
            url: '/learner-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.learnerInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learner-info/learner-infos.html',
                    controller: 'LearnerInfoController',
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
                    $translatePartialLoader.addPart('learnerInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('learner-info-detail', {
            parent: 'learner-info',
            url: '/learner-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.learnerInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learner-info/learner-info-detail.html',
                    controller: 'LearnerInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('learnerInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LearnerInfo', function($stateParams, LearnerInfo) {
                    return LearnerInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'learner-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('learner-info-detail.edit', {
            parent: 'learner-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-info/learner-info-dialog.html',
                    controller: 'LearnerInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LearnerInfo', function(LearnerInfo) {
                            return LearnerInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learner-info.new', {
            parent: 'learner-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-info/learner-info-dialog.html',
                    controller: 'LearnerInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                time: null,
                                exerciseData: null,
                                bodytestData: null,
                                coachAdvice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('learner-info', null, { reload: 'learner-info' });
                }, function() {
                    $state.go('learner-info');
                });
            }]
        })
        .state('learner-info.edit', {
            parent: 'learner-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-info/learner-info-dialog.html',
                    controller: 'LearnerInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LearnerInfo', function(LearnerInfo) {
                            return LearnerInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learner-info', null, { reload: 'learner-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learner-info.delete', {
            parent: 'learner-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-info/learner-info-delete-dialog.html',
                    controller: 'LearnerInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LearnerInfo', function(LearnerInfo) {
                            return LearnerInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learner-info', null, { reload: 'learner-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
