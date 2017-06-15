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
            .state('registration-detail',{
                parent:'student-registration',
                url:'/registration/{id}',
                data:{authorities:['ROLE_USER']},
                views:{
                    'content@':{
                        templateUrl:'app/page/student-registration/registration.html',
                        controller:'registrationController',
                        controllerAs:'vm'

                    }
                },
                resolve:{
                    translatePartialLoader:['$translate', '$translatePartialLoader',function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('course');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'coursesService',function ($stateParams, coursesService) {
                        return coursesService.loadCourse()({id : $stateParams.id}).$promise;
                    }],
                    previousState:["$state",function ($state) {
                        var currentStateData = {
                            name:$state.current.name || 'course',
                            params:$state.params,
                            url:$state.href($state.current.name,$state.params)
                        };
                        return currentStateData;
                    }]
                }
            });

    }]);
})();
