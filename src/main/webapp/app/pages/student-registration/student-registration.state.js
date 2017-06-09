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
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                    }]
                }
            })
            .state('course.registration', {
                parent: 'student-registration',
                url: '/{id}/registration',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/student-registration/registration.html',
                        controller: 'registrationController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Course', function(Course) {
                                return Course.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('student-registration', null, { reload: 'student-registration' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('student-registration.registration', {
                parent: 'student-registration',
                url: '/student-registration.registration',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/pages/student-registration/registration.html',
                        controller: 'registrationController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    coachName: null,
                                    coachPhone: null,
                                    coachIntroduce: null,
                                    coachPicture: null,
                                    coachWechatopenid: null,
                                    coachWechatname: null,
                                    coachWechatpicture: null,
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

            });
    }]);
})();
