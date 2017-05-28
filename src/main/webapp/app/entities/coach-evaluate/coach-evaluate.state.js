(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coach-evaluate', {
            parent: 'entity',
            url: '/coach-evaluate?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.coachEvaluate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coach-evaluate/coach-evaluates.html',
                    controller: 'CoachEvaluateController',
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
                    $translatePartialLoader.addPart('coachEvaluate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coach-evaluate-detail', {
            parent: 'coach-evaluate',
            url: '/coach-evaluate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.coachEvaluate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coach-evaluate/coach-evaluate-detail.html',
                    controller: 'CoachEvaluateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coachEvaluate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CoachEvaluate', function($stateParams, CoachEvaluate) {
                    return CoachEvaluate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'coach-evaluate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('coach-evaluate-detail.edit', {
            parent: 'coach-evaluate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach-evaluate/coach-evaluate-dialog.html',
                    controller: 'CoachEvaluateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoachEvaluate', function(CoachEvaluate) {
                            return CoachEvaluate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coach-evaluate.new', {
            parent: 'coach-evaluate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach-evaluate/coach-evaluate-dialog.html',
                    controller: 'CoachEvaluateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                serviceAttitude: null,
                                speciality: null,
                                like: null,
                                complain: null,
                                evaluation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('coach-evaluate', null, { reload: 'coach-evaluate' });
                }, function() {
                    $state.go('coach-evaluate');
                });
            }]
        })
        .state('coach-evaluate.edit', {
            parent: 'coach-evaluate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach-evaluate/coach-evaluate-dialog.html',
                    controller: 'CoachEvaluateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoachEvaluate', function(CoachEvaluate) {
                            return CoachEvaluate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('coach-evaluate', null, { reload: 'coach-evaluate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coach-evaluate.delete', {
            parent: 'coach-evaluate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach-evaluate/coach-evaluate-delete-dialog.html',
                    controller: 'CoachEvaluateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CoachEvaluate', function(CoachEvaluate) {
                            return CoachEvaluate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('coach-evaluate', null, { reload: 'coach-evaluate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
