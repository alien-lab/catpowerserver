/**
 * Created by asus on 2017/6/2.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider.state('student-withdrawal', {
            parent: 'app',
            url: '/student-withdrawal',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/student-withdrawal/student-withdrawal.html',
                    controller: 'studentWithdrawalController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('student-withdrawal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }]);
})();
