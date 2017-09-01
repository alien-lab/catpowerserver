/**
 * Created by 橘 on 2017/5/31.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.controller("shopOpController",["$http","CourseScheduling","$scope","$filter","shopopService","BuyCourse","buyCourseTodayService","AlertService","qrService","ticket","pagingParams","ParseLinks","$state","$window",
        function($http,CourseScheduling,$scope,$filter,shopopService,BuyCourse,buyCourseTodayService,AlertService,qrService,ticket,pagingParams,ParseLinks,$state,$window){
        var vm = this;

        $scope.outCourse = null;
        $scope.buyCourseCountToday = null;
        $scope.haveBuyCourse=false;
        $scope.noneBuyCourse = false;

        $scope.$on('catpowerserverApp:courseSchedulingUpdate', function(e, msg) {
            loadSche();
        });

        //获取签到二维码
        $scope.signQr = function (arrangment) {
            var scheCourseName=arrangment.sche.course.courseName;
            qrService.loadSignQr(arrangment.sche.id,function (data) {
                if(arrangment.showQr==null)arrangment.showQr=false;
                arrangment.showQr=!arrangment.showQr;
                arrangment.arrangementCourseQr = ticket+data.qrTicker;
            })
        };

        //下课
        $scope.getOutClass = function (id) {
            this.outCourse = true;
            //插入排课结束时间
            var scheId = id;
            shopopService.addEndCourseTime(scheId,function (data,flag) {
                if(!flag){
                    // alert(data);
                }
            });
            var index = -1;
            if(this.arrangement.sche.status == '已下课' ){
                //alert(1);
            }
            angular.forEach($scope.coachArrangements,function (item,key) {
                if(item.sche.id == id ){
                    index = key;
                }
            });
            if(index != -1){
                $scope.coachArrangements[index].sche.status = '已下课';
            }

        };
        //取消教练排课
        $scope.removeCoachArrangement = function (id) {
            var index = -1;
            angular.forEach($scope.coachArrangements,function (item,key) {
                if(item.id == id ){
                    index = key;
                }
            });
            if(index != -1){
                $scope.coachArrangements.splice(index,1);
            }
            return $scope.coachArrangements;
        };
        //添加学员
        $scope.addStu = function (scheId) {
            var index = -1;
            angular.forEach($scope.coachArrangements,function (item,key) {
                if(item.sche.id == scheId ){
                    index = key;
                }
            });
            if(index != -1){
                console.log($scope.coachArrangements[index]);
            }
        }


        loadSche();
        function loadSche(date){
            //今日签到与建档人数
            shopopService.loadRegisterCount(date,function (data,flag) {
                if(!flag){
                   // alert(data);
                }
                $scope.registerCount =data;
            });
            //今日销售的数量及总金额
            shopopService.loadBuyCourseCountToday(date,function (data,flag) {
                if(!flag){
                    //alert(data);
                }
                $scope.buyCourseCountToday = data;
                console.log($scope.buyCourseCountToday)

                if($scope.buyCourseCountToday.buycount == 0){
                    $scope.noneBuyCourse=true;
                }else{
                    $scope.haveBuyCourse=true;
                }

            });
            //今日教练排课
            shopopService.loadCoachArrangement(date,function(data,flag){
                if(!flag){
                   // alert(data);
                }
                $scope.coachArrangements = data;
                console.log($scope.coachArrangements);
            });
        }

        /**
         * 今日售课
         */
        $scope.buyCoursesToday = [];
        buyCourseTodayService.loadBuyCourseToday(0,100,function (data) {
            $scope.buyCoursesToday =data;
            console.log($scope.buyCoursesToday)
        });


    }]);

    //今日售课
    app.factory("buyCourseTodayResource",["$resource",function($resource){
        var resourceUrl =  'api/buy-courses/today';
        return $resource(resourceUrl, {}, {
            'getBuyCourseToday': { method: 'GET'}
        });

    }]);
    app.service("buyCourseTodayService",["buyCourseTodayResource",function(buyCourseTodayResource){
        this.loadBuyCourseToday=function(index,size,callback){
            buyCourseTodayResource.getBuyCourseToday({
                index:index,
                size:size
            },function(data){
                if(callback){
                    callback(data,true);
                }
            },function(error){
                console.log("shopopResource.getBuyCourseToday()",error);
                if(callback){
                    callback(error,false);
                }
            })
        }
    }]);
    //获取签到二维码
    app.factory("qrResource",["$resource",function($resource){
        var resourceUrl =  'api/course-schedulings/qr/scheId';
        return $resource(resourceUrl, {}, {
            'getQr': { method: 'GET'}
        });

    }]);
    app.service("qrService",["qrResource",function (qrResource) {
        this.loadSignQr = function (scheId,callback) {
            qrResource.getQr({
                scheId:scheId
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("qrResource.getQr()"+error);
                if(callback){
                    callback(error,false);
                }
            });
        }
    }]);
})();

(function () {
    'use strict'
    var app=angular.module('catpowerserverApp');
    /**
     * 今日教练排课
     */
    app.service("shopopService",["$http",function ($http) {
        //今日教练排课
        this.loadCoachArrangement = function (date,callback) {
            $http({
                url:"api/course-schedulings/today",
                method:"GET"
            }).then(function(data){
                if(callback){
                    callback(data.data,true);
                }
            },function(error){
                if(callback){
                    callback(error,false);
                }
            })
        };
        //今日签到与建档人数
        this.loadRegisterCount = function (date,callback) {
            $http({
                url:"api/learners/count/today",
                method:"GET"
            }).then(function(data){
                if(callback){
                    callback(data.data,true);
                }
            },function(error){
                if(callback){
                    callback(error,false);
                }
            })
        };
        //今日销售的数量及总金额
        this.loadBuyCourseCountToday = function (date,callback) {
            $http({
                url:'api/buy-courses/today/count',
                method:'GET'
            }).then(function (data) {
                if(callback){
                    callback(data.data,true);
                }
            },function (error) {
                if(callback){
                    callback(error,false);
                }
            });
        };
        //插入教练排课结束时间
        this.addEndCourseTime = function (scheId,callback) {
            $http({
                url:"api/course-schedulings/courseScheduling/"+scheId,
                method:"PUT"
            }).then(function (data) {
                if(callback){
                    callback(data.data,true);
                }
            },function (error) {
                if(callback){
                    callback(error,false);
                }
            });
        };
        //更新上课的状态
        /*this.updateCourseStatus = function (id,status,callback) {
            $http({
                url:"api/course-schedulings/courseStatus/"+id+"/"+status,
                method:"PUT"
            }).then(function (data) {
                if(callback){
                    callback(data.data,true);
                }
            },function (error) {
                if(callback){
                    callback(error,false);
                }
            });
        }*/

    }]);

})();


