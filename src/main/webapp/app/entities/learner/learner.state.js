(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('learner', {
            parent: 'entity',
            url: '/learner?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.learner.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learner/learners.html',
                    controller: 'LearnerController',
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
                    $translatePartialLoader.addPart('learner');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('learner-detail', {
            parent: 'learner',
            url: '/learner/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.learner.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learner/learner-detail.html',
                    controller: 'LearnerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('learner');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Learner', function($stateParams, Learner) {
                    return Learner.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'learner',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('learner-detail.edit', {
            parent: 'learner-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner/learner-dialog.html',
                    controller: 'LearnerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Learner', function(Learner) {
                            return Learner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learner.new', {
            parent: 'learner',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner/learner-dialog.html',
                    controller: 'LearnerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                learneName: null,
                                learnerPhone: null,
                                learnerSex: null,
                                registTime: null,
                                wxOpenId: null,
                                wxNickname: null,
                                wxHeader: null,
                                firstTotime: null,
                                firstBuyclass: null,
                                recentlySignin: null,
                                experience: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('learner', null, { reload: 'learner' });
                }, function() {
                    $state.go('learner');
                });
            }]
        })
        .state('learner.edit', {
            parent: 'learner',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner/learner-dialog.html',
                    controller: 'LearnerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Learner', function(Learner) {
                            return Learner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learner', null, { reload: 'learner' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learner.delete', {
            parent: 'learner',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learner/learner-delete-dialog.html',
                    controller: 'LearnerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Learner', function(Learner) {
                            return Learner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learner', null, { reload: 'learner' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
