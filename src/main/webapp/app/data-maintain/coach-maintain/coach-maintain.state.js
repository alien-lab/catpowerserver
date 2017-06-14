/**
 * Created by asus on 2017/6/11.
 */
(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('coach-maintain', {
                parent: 'data-maintain',
                url: '/data-maintain',
                data: {
                    authorities: ['ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/data-maintain/coach-maintain/coach-maintain.html',
                        controller: 'coachMaintainController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coach-maintain');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('coach-maintain.new',{
                parent:'coach-maintain',
                url:'/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter:['$stateParams', '$state', '$uibModal',function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/coach-maintain/coach-maintain-dialog.html',
                        controller: 'coachMaintainDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size:'md',
                        resolve:{
                            dataMaintain : function () {
                                return {
                                    coachIntroduce:null,
                                    coachName: null,
                                    coachPhone: null,
                                    coachPicture: null,
                                    coachWechatname: null,
                                    coachWechatopenid: null,
                                    coachWechatpicture: null,
                                    id:null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('coach-maintain', null, { reload: 'coach-maintain' });
                    }, function() {
                        $state.go('coach-maintain');
                    });
                }]
            })
            .state('coach-maintain-detail',{
                parent:'coach-maintain',
                url:'/coach-maintain/{id}',
                data:{
                    authorities:['ROLE_USER'],
                    pageTitle:'教练详情'
                },
                views:{
                    'content@':{
                        templateUrl:'app/data-maintain/coach-maintain/coach-maintain-detail.html',
                        controller:'coachMaintainDetailController',
                        controllerAs:'vm'
                    }
                },
                resolve:{
                    translatePartialLoader:['$translate', '$translatePartialLoader',function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coach');
                        return $translate.refresh();
                    }],
                    dataMaintain: ['$stateParams', 'Coach', function($stateParams, Coach) {
                        return Coach.get({id : $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'coach',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('coach-maintain.edit', {
                parent: 'coach-maintain',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '教练编辑'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/coach-maintain/coach-maintain-dialog.html',
                        controller: 'coachMaintainDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            dataMaintain: ['Coach', function(Coach) {
                                return Coach.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('coach-maintain', null, { reload: 'coach-maintain' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('coach-maintain-detail.edit',{
                parent:'coach-maintain-detail',
                url:'/coach-maintain-detail/edit',
                data:{
                    authorities: ['ROLE_USER'],
                    pageTitle: '教练编辑'
                },
                onEnter:['$stateParams', '$state', '$uibModal',function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/coach-maintain/coach-maintain-dialog.html',
                        controller: 'coachMaintainDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve:{
                            dataMaintain:['Coach',function (Coach) {
                                return Coach.get({id:$stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, { reload: false });
                    },function () {
                        $state.go('^');
                    })
                }]
            })
            .state('coach-maintain.delete',{
                parent: 'coach-maintain',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/coach-maintain/coach-maintain-delete.html',
                        controller: 'coachMaintainDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            dataMaintain: ['Coach', function(Coach) {
                                return Coach.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('coach-maintain', null, { reload: 'coach-maintain' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();
