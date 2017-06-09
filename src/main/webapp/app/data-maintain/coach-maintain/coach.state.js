/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.config(['$stateProvider',function ($stateProvider) {

        $stateProvider
            .state('coach-maintain',{
            parent:'app',
            url:'/coach-maintain?page&sort&search',
            data:{
                authorities:['ROLE_USER']
            },
            views:{
                'content@':{
                    templateUrl:'app/data-maintain/coach-maintain/coach.html',
                    controller:'coachController',
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
