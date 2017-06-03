/**
 * Created by asus on 2017/6/1.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider.state('coach-arrangement', {
            parent: 'app',
            url: '/coach-arrangement',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/coach-arrangement/coach-arrangement.html',
                    controller: 'coachArrangementController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coachArrangement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }]);
})();
