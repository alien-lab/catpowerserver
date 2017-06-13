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
                        size:'lg',
                        resolve:{
                            dataMaintain : function () {
                                return {
                                    coachId:null,
                                    coachName: null,
                                    coachPhone: null,
                                    coachDescription: null,
                                    coachPicture: null,
                                    favourableComment: null,
                                    negativeComment: null,
                                    professionalLevel: null,
                                    serviceAttitude:null,
                                    likesIndex: null,
                                    wechatOpenId:null,
                                    wechatName:null,
                                    wechatPicture:null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('coach-maintain', null, { reload: 'coach-maintain' });
                    }, function() {
                        $state.go('coach-maintain');
                    });
                }]
            });
    }

})();
