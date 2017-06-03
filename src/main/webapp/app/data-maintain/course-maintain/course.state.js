/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.config(['$stateProvider',function ($stateProvider) {

        $stateProvider.state('course-maintain',{
            parent:'app',
            url:'/course-maintain',
            data:{
                authorities:['ROLE_USER']
            },
            views:{
                'content@':{
                    templateUrl:'app/data-maintain/course-maintain/course.html',
                    controller:'courseController'
                }
            },
            resolve:{
                translatePartialLoader:['$translate','$translatePartialLoader',function () {
                    $translatePartialLoader.addPart('course-maintain');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });

    }]);
})();
