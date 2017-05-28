(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('buy-course', {
            parent: 'entity',
            url: '/buy-course?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.buyCourse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/buy-course/buy-courses.html',
                    controller: 'BuyCourseController',
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
                    $translatePartialLoader.addPart('buyCourse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('buy-course-detail', {
            parent: 'buy-course',
            url: '/buy-course/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'catpowerserverApp.buyCourse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/buy-course/buy-course-detail.html',
                    controller: 'BuyCourseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('buyCourse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BuyCourse', function($stateParams, BuyCourse) {
                    return BuyCourse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'buy-course',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('buy-course-detail.edit', {
            parent: 'buy-course-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buy-course/buy-course-dialog.html',
                    controller: 'BuyCourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BuyCourse', function(BuyCourse) {
                            return BuyCourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('buy-course.new', {
            parent: 'buy-course',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buy-course/buy-course-dialog.html',
                    controller: 'BuyCourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                paymentWay: null,
                                paymentAccount: null,
                                buyTime: null,
                                status: null,
                                operator: null,
                                operateContent: null,
                                operateTime: null,
                                remainClass: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('buy-course', null, { reload: 'buy-course' });
                }, function() {
                    $state.go('buy-course');
                });
            }]
        })
        .state('buy-course.edit', {
            parent: 'buy-course',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buy-course/buy-course-dialog.html',
                    controller: 'BuyCourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BuyCourse', function(BuyCourse) {
                            return BuyCourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('buy-course', null, { reload: 'buy-course' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('buy-course.delete', {
            parent: 'buy-course',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/buy-course/buy-course-delete-dialog.html',
                    controller: 'BuyCourseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BuyCourse', function(BuyCourse) {
                            return BuyCourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('buy-course', null, { reload: 'buy-course' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
