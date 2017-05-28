(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('course-atlas', {
            parent: 'entity',
            url: '/course-atlas?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.courseAtlas.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-atlas/course-atlases.html',
                    controller: 'CourseAtlasController',
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
                    $translatePartialLoader.addPart('courseAtlas');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('course-atlas-detail', {
            parent: 'course-atlas',
            url: '/course-atlas/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.courseAtlas.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-atlas/course-atlas-detail.html',
                    controller: 'CourseAtlasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('courseAtlas');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CourseAtlas', function($stateParams, CourseAtlas) {
                    return CourseAtlas.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'course-atlas',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('course-atlas-detail.edit', {
            parent: 'course-atlas-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-atlas/course-atlas-dialog.html',
                    controller: 'CourseAtlasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseAtlas', function(CourseAtlas) {
                            return CourseAtlas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-atlas.new', {
            parent: 'course-atlas',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-atlas/course-atlas-dialog.html',
                    controller: 'CourseAtlasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                picture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('course-atlas', null, { reload: 'course-atlas' });
                }, function() {
                    $state.go('course-atlas');
                });
            }]
        })
        .state('course-atlas.edit', {
            parent: 'course-atlas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-atlas/course-atlas-dialog.html',
                    controller: 'CourseAtlasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseAtlas', function(CourseAtlas) {
                            return CourseAtlas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-atlas', null, { reload: 'course-atlas' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-atlas.delete', {
            parent: 'course-atlas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-atlas/course-atlas-delete-dialog.html',
                    controller: 'CourseAtlasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CourseAtlas', function(CourseAtlas) {
                            return CourseAtlas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-atlas', null, { reload: 'course-atlas' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
