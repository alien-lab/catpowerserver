/**
 * Created by asus on 2017/7/22.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider
            .state('coach-workSche', {
                parent: 'app',
                url: '/coach-workSche',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/pages/coach-workSche/coach-workSche.html',
                        controller: 'coachWorkScheController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
    }]);
})();
