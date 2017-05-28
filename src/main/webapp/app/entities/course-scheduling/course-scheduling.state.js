(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('course-scheduling', {
            parent: 'entity',
            url: '/course-scheduling?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.courseScheduling.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-scheduling/course-schedulings.html',
                    controller: 'CourseSchedulingController',
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
                    $translatePartialLoader.addPart('courseScheduling');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('course-scheduling-detail', {
            parent: 'course-scheduling',
            url: '/course-scheduling/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.courseScheduling.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/course-scheduling/course-scheduling-detail.html',
                    controller: 'CourseSchedulingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('courseScheduling');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CourseScheduling', function($stateParams, CourseScheduling) {
                    return CourseScheduling.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'course-scheduling',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('course-scheduling-detail.edit', {
            parent: 'course-scheduling-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-scheduling/course-scheduling-dialog.html',
                    controller: 'CourseSchedulingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseScheduling', function(CourseScheduling) {
                            return CourseScheduling.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-scheduling.new', {
            parent: 'course-scheduling',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-scheduling/course-scheduling-dialog.html',
                    controller: 'CourseSchedulingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                status: null,
                                qrCode: null,
                                signInCount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('course-scheduling', null, { reload: 'course-scheduling' });
                }, function() {
                    $state.go('course-scheduling');
                });
            }]
        })
        .state('course-scheduling.edit', {
            parent: 'course-scheduling',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-scheduling/course-scheduling-dialog.html',
                    controller: 'CourseSchedulingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CourseScheduling', function(CourseScheduling) {
                            return CourseScheduling.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-scheduling', null, { reload: 'course-scheduling' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('course-scheduling.delete', {
            parent: 'course-scheduling',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/course-scheduling/course-scheduling-delete-dialog.html',
                    controller: 'CourseSchedulingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CourseScheduling', function(CourseScheduling) {
                            return CourseScheduling.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('course-scheduling', null, { reload: 'course-scheduling' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
