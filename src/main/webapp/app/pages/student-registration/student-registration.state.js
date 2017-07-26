/**
 * Created by asus on 2017/6/2.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider
            .state('student-registration', {
            parent: 'app',
            url: '/student-registration',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/student-registration/student-registration.html',
                    controller: 'studentRegistrationController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('student-registration');
                    $translatePartialLoader.addPart('buyCourse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                    }]
                }
            })
            .state('student-registration.new', {
                parent: 'student-registration',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/student-registration/student-registration-dialog.html',
                        controller: 'BuyCourseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
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
                        $state.go('student-registration', null, { reload: 'student-registration' });
                    }, function() {
                        $state.go('student-registration');
                    });
                }]
            })
            .state('student-registration.detail',{
                parent:'student-registration',
                url:'/student-registration/{id}',
                data:{
                    authorities:['ROLE_USER'],
                    pageTitle:'课程详情'
                },
                views:{
                    'content@':{
                        templateUrl:'app/pages/student-registration/student-registration-detail.html',
                        controller:'courseMaintainDetailController',
                        controllerAs:'vm'
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('course');
                        return $translate.refresh();
                    }],
                    dataMaintain: ['$stateParams', 'Course', function($stateParams, Course) {
                        return Course.get({id : $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'course',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('student-registration.add', {
                parent: 'student-registration',
                url: '/{id}/add',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/student-registration/student-registration.new.html',
                        controller: 'studentBuyCourseController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
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
                            },
                            courseEntity: ['Course', function(Course) {
                                return Course.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('student-registration', null, { reload: 'student-registration' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });

    }]);
})();
