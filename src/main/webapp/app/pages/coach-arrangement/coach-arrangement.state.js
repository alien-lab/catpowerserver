/**
 * Created by asus on 2017/6/1.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider
            .state('coach-arrangement', {
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
                        $translatePartialLoader.addPart('courseScheduling');
                        $translatePartialLoader.addPart('coachArrangement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('coach-arrangement.new',{
                parent:'coach-arrangement',
                url:'/new',
                data:{authorities:['ROLE_USER']},
                onEnter:['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal){
                    $uibModal.open({
                        templateUrl:'app/pages/coach-arrangement/coach-arrangement-dialog.html',
                        controller:'arrangementController',
                        controllerAs:'vm',
                        backdrop:'static',
                        size:'md',
                        resolve:{
                            entity: function () {
                                return {
                                    startTime: null,
                                    endTime: null,
                                    status: null,
                                    qrCode: null,
                                    signInCount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('coach-arrangement',null,{reload:'coach-arrangement'});
                    },function () {
                        $state.go('coach-arrangement');
                    })
                }]
            });
    }]);
})();
