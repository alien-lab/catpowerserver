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
            .state('course-maintain', {
                parent: 'data-maintain',
                url: '/course-maintain',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '课程维护'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/data-maintain/course-maintain/course-maintain.html',
                        controller: 'courseMaintainController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('course-maintain');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('course-maintain.new',{
                parent:'course-maintain',
                url:'/new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '创建课程'
                },
                onEnter:['$stateParams', '$state', '$uibModal',function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/course-maintain/course-maintain-dialog.html',
                        controller: 'courseMaintainDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size:'lg',
                        resolve:{
                            dataMaintain : function () {
                                return {
                                    classNumber:null,
                                    courseIntroductions:null,
                                    courseOtherInfo:null,
                                    coursePrices:null,
                                    courseVipprices:null,
                                    id:null,
                                    totalClassHour:null,
                                    courseThumbnail:null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('course-maintain', null, { reload: 'course-maintain' });
                    }, function() {
                        $state.go('course-maintain');
                    });
                }]
            })
            .state('course-maintain-detail',{
                parent:'course-maintain',
                url:'/course-maintain/{id}',
                data:{
                    authorities:['ROLE_USER'],
                    pageTitle:'课程详情'
                },
                views:{
                    'content@':{
                        templateUrl:'app/data-maintain/course-maintain/course-maintain-detail.html',
                        controller:'courseMaintainDetailController',
                        controllerAs:'vm'
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('course');
                        return $translate.refresh();
                    }],
                    dataMaintain: ['$stateParams', 'Course', function($stateParams, Course) {
                        return Course.get({id : $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'course',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('course-maintain.edit', {
                parent: 'course-maintain',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '编辑课程'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/course-maintain/course-maintain-dialog.html',
                        controller: 'courseMaintainDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            dataMaintain: ['Course', function(Course) {
                                return Course.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('course-maintain', null, { reload: 'course-maintain' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('course-maintain-detail.edit',{
                parent:'course-maintain-detail',
                url:'/course-maintain-detail/edit',
                data:{
                    authorities: ['ROLE_USER'],
                    pageTitle: '编辑课程'
                },
                onEnter:['$stateParams', '$state', '$uibModal',function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/course-maintain/course-maintain-dialog.html',
                        controller: 'courseMaintainDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve:{
                            dataMaintain:['Course',function (Course) {
                                return Course.get({id:$stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, { reload: false });
                    },function () {
                        $state.go('^');
                    })
                }]
            })
            .state('course-maintain.delete',{
                parent: 'course-maintain',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/data-maintain/course-maintain/course-maintain-delete.html',
                        controller: 'courseMaintainDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            dataMaintain: ['Course', function(Course) {
                                return Course.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('course-maintain', null, { reload: 'course-maintain' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();
