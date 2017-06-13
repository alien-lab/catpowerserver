/**
 * Created by asus on 2017/6/2.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider
            .state('student-filing', {
                parent: 'app',
                url: '/student-filing',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/pages/student-filing/student-filing.html',
                        controller: 'studentFilingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('student-filing');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('student-filing.new',{
                parent:'student-filing',
                url:'/new',
                data:{
                    authorities:['ROLE_USER']
                },
                onEnter:['$stateParams','$state','$uibModal',function ($stateParams,$state,$uibModal) {
                    $uibModal.open({
                        templateUrl:'app/pages/student-filing/student-dialog.html',
                        controller:'studentFilingDialogController',
                        controllerAs:'vm',
                        backdrop:'static',
                        size:'lg',
                        resolve:{
                            student:function () {
                                return{
                                    stuName:null,
                                    stuSex:null,
                                    stuPhone:null,
                                    registrationTime:null,
                                    firstGoShopTime:null,
                                    totalEmpiricalValue:null
                                }
                            }
                        }
                    }).result.then(function () {
                        $state.go('student-filing',null,{reload:'student-filing'});
                    },function () {
                        $state.go('student-filing');
                    });
                }]
            })
    }]);
})();
