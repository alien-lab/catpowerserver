(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('learner-charge', {
            parent: 'entity',
            url: '/learner-charge?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.learnerCharge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learner-charge/learner-charges.html',
                    controller: 'LearnerChargeController',
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
                    $translatePartialLoader.addPart('learnerCharge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('learner-charge-detail', {
            parent: 'learner-charge',
            url: '/learner-charge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.learnerCharge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learner-charge/learner-charge-detail.html',
                    controller: 'LearnerChargeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('learnerCharge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LearnerCharge', function($stateParams, LearnerCharge) {
                    return LearnerCharge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'learner-charge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('learner-charge-detail.edit', {
            parent: 'learner-charge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-charge/learner-charge-dialog.html',
                    controller: 'LearnerChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LearnerCharge', function(LearnerCharge) {
                            return LearnerCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learner-charge.new', {
            parent: 'learner-charge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-charge/learner-charge-dialog.html',
                    controller: 'LearnerChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                chargeTime: null,
                                buyCourseId: null,
                                chargePeople: null,
                                remainNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('learner-charge', null, { reload: 'learner-charge' });
                }, function() {
                    $state.go('learner-charge');
                });
            }]
        })
        .state('learner-charge.edit', {
            parent: 'learner-charge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-charge/learner-charge-dialog.html',
                    controller: 'LearnerChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LearnerCharge', function(LearnerCharge) {
                            return LearnerCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learner-charge', null, { reload: 'learner-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learner-charge.delete', {
            parent: 'learner-charge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner-charge/learner-charge-delete-dialog.html',
                    controller: 'LearnerChargeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LearnerCharge', function(LearnerCharge) {
                            return LearnerCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learner-charge', null, { reload: 'learner-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
