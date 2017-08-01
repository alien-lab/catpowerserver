(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider
            .state('shopop', {
                parent: 'app',
                url: '/',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/pages/shop/shop.html',
                        controller: 'shopOpController',
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
                        $translatePartialLoader.addPart('shop');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('learnerCharge');
                        $translatePartialLoader.addPart('courseScheduling');
                        return $translate.refresh();
                    }]
                }
            })
            .state('shopop.buyCourses', {
                url: 'buyCourses',
                templateUrl: 'app/pages/shop/shopBuyCourses.html',
                controller: 'shopBuyCourseController',
                controllerAs: 'vm',
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
                    }]
                }
            })
            .state('shopop.sche', {
                url: 'sche',
                templateUrl: 'app/pages/shop/sche.html',
                controller: 'scheController',
                controllerAs: 'vm',
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
                resolve:{
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }]
                }
            })
            .state('shopop.new', {
                parent: 'shopop',
                url: 'new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/shop/addTodayCourse.html',
                        controller: 'addCourseController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            addArrangement: function () {
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
                        $state.go('shopop', null, { reload: 'shopop' });
                    }, function() {
                        $state.go('shopop');
                    });
                }]
            })
            .state('shopop.charge-new', {
                parent: 'shopop',
                url: '{id}/charge-new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/shop/addCourseCharge.html',
                        controller: 'LearnerChargeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    chargeTime: null,
                                    buyCourseId: null,
                                    chargePeople: null,
                                    remainNumber: null,
                                    id: null
                                };
                            },
                            entityCourseScheduling: ['CourseScheduling', function(CourseScheduling) {
                                return CourseScheduling.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('shopop', null, { reload: 'shopop' });
                    }, function() {
                        $state.go('shopop');
                    });
                }]
            })
            .state('shopop.schedulingDelete', {
                parent: 'shopop',
                url: '{id}/scheduling-delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/shop/shop-coachArrangement-delete.html',
                        controller: 'CourseSchedulingDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['CourseScheduling', function(CourseScheduling) {
                                return CourseScheduling.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('shopop', null, { reload: 'shopop' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('shopop.chargeDelete', {
                parent: 'shopop',
                url: '{id}/learner-delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/shop/shop-coachArrangement-learnerDelete.html',
                        controller: 'LearnerChargeDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['LearnerCharge', function(LearnerCharge) {
                                return LearnerCharge.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('shopop', null, { reload: 'shopop' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }]);
})();
