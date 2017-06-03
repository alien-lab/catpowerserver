/**
 * Created by asus on 2017/6/2.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider.state('student-registration', {
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
    }]);
})();
