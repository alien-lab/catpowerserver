/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.config(['$stateProvider',function ($stateProvider) {

        $stateProvider.state('coach-maintain',{
            parent:'app',
            url:'/coach-maintain',
            data:{
                authorities:['ROLE_USER']
            },
            views:{
                'content@':{
                    templateUrl:'app/data-maintain/coach-maintain/coach.html',
                    controller:'coachController'
                }
            },
            resolve:{
                translatePartialLoader:['$translate','$translatePartialLoader',function () {
                    $translatePartialLoader.addPart('coach-maintain');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });

    }]);
})();