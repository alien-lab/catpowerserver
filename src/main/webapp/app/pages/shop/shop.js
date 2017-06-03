/**
 * Created by 橘 on 2017/5/31.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller("shopOpController",["$scope","shopopService","coachArrangementService","classesTodayService","traineeTodayService",function($scope,shopopService,coachArrangementService,classesTodayService,traineeTodayService){
        //今日教练排课
        $scope.coachArrangements = coachArrangementService.loadCoachArrangement();
        //今日售课
        $scope.sellingCoursers = classesTodayService.loadSellingCoursers();
        //今日学员
        $scope.traineeTodays = traineeTodayService.loadTraineeToday();
        $scope.page={
            index:0,
            size:10
        };

        function loadbuycourse(){
            shopopService.loadbuycourse($scope.page.index,$scope.page.size,function(data){

            });
        }

        $scope.loadbuycourse=loadbuycourse;
        loadbuycourse();




        //

    }]);
    app.service("traineeTodayService",[function () {
        this.loadTraineeToday = function () {
            var traineeTodays = [{
                traineeName1:'张三',
                traineeName2:'李四',
                traineeName3:'王五',

            },{
                traineeName1:'cll'
            }];
            return traineeTodays;
        }
    }]);
    app.service("coachArrangementService",[function () {
        this.loadCoachArrangement = function () {
            var coachArrangements = [{
                coachName:'鞠董',
                courseName:'瑜伽',
                courseState:'上课中',
                time:'2017-6-2 18:00',
                traineeCount:'1',
                traineeName1:'张三'

            },{
                coachName:'鞠董',
                courseName:'瑜伽',
                courseState:'上课中',
                time:'2017-6-2 18:00',
                traineeCount:'1',
                traineeName1:'陈乐乐'
            }];
            return coachArrangements;
        }
    }]);
    app.service("classesTodayService",[function () {
        this.loadSellingCoursers = function () {
            var sellingCoursers = [{
                traineeName:'张三',
                courseName:'瑜伽',
                coachName:'鞠董',
                courseCount:'1',
                money:'5000',
                time:'2017-6-2 19:00'
            },{
                traineeName:'张三',
                courseName:'瑜伽',
                coachName:'鞠董',
                courseCount:'1',
                money:'5000',
                time:'2017-6-2 19:00'
            },{
                traineeName:'张三',
                courseName:'瑜伽,体操技能',
                coachName:'鞠董',
                courseCount:'2',
                money:'12000',
                time:'2017-6-2 19:00'
            }];
            return sellingCoursers;
        }
    }]);

    app.service("shopopService",["shopopResource",function(shopopResource){
        this.loadbuycourse=function(index,size,callback){
            shopopResource.query({
                index:index,
                size:size
            },function(data){
                console.log("shopopResource.query()",data);
                if(callback){
                    callback(data,true);
                }
            },function(error){
                console.log("shopopResource.query()",error);
                if(callback){
                    callback(error,false);
                }
            })
        }
    }]);


    app.factory("shopopResource",["$resource",function($resource){
        var resourceUrl =  'api/buy-courses/today';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'}
        });
    }]);


})();


(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.config(["$stateProvider",function($stateProvider){
        $stateProvider.state('shopop', {
            parent: 'app',
            url: '/',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/shop/shop.html',
                    controller: 'shopOpController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shop');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shopop.new', {
            parent: 'shopop',
            url: 'shopop.new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/shop/addTodayCourse.html',
                    controller: 'addCourseController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                coachName: null,
                                coachPhone: null,
                                coachIntroduce: null,
                                coachPicture: null,
                                coachWechatopenid: null,
                                coachWechatname: null,
                                coachWechatpicture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shopop', null, { reload: 'shopop' });
                }, function() {
                    $state.go('shopop');
                });
            }]
        })
    }]);
})();
