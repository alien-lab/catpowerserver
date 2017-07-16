/**
 * Created by 橘 on 2017/5/31.
 */
(function(){
    'use strict';
    var app=angular.module('catpowerserverApp');

    app.controller("shopOpController",["$http","CourseScheduling","$scope","$filter","shopopService","BuyCourse","buyCourseService","AlertService","qrService", function($http,CourseScheduling,$scope,$filter,shopopService,BuyCourse,buyCourseService,AlertService,qrService){
        var vm = this;
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
            });
            //今日教练排课
            shopopService.loadCoachArrangement(date,function(data,flag){
                if(!flag){
                   // alert(data);
                }
                $scope.coachArrangements = data;
                console.log($scope.coachArrangements);
                //获取签到二维码
                $scope.signQr = function (scheId) {
                    var index = -1;
                    var scheCourseName;
                    angular.forEach($scope.coachArrangements,function (item,key) {
                        if(item.sche.id == scheId){
                            index = key;
                            scheCourseName = item.sche.course.courseName;
                        }
                    });
                    if(index != -1){
                        qrService.loadSignQr(scheId,function (data) {
                            $scope.qr=data;
                            console.log($scope.qr);
                            console.log($scope.qr.qrTicker);
                            $scope.arrangementCourseQr = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+$scope.qr.qrTicker;
                            console.log($scope.arrangementCourseQr);
                            $scope.scheCourseName = scheCourseName;
                        })
                    }
                };
                //下课
                $scope.getOutClass = function (id) {

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
                        $http({
                            url:"api/course-schedulings/courseStatus/"+$scope.coachArrangements[index].sche.id,
                            method:"PUT",
                            data:$scope.coachArrangements[index].sche.status
                        }).success(function (data) {
                            console.log(data);
                        });
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
            });
        }

            $scope.page={
            index:0,
            size:10
        };

        //获取当前日期
        var now = new Date();
        var month = now.getMonth()+1;
        var nowDate = now.getFullYear()+'-'+month+'-'+now.getDate();
        $scope.nowDate = nowDate;
        //时段
        $scope.today = function() {
            $scope.startDate = new Date();
            $scope.endDate = new Date();
            $scope.startArrangementCourseDate = new Date();
            $scope.endArrangementCourseDate = new Date();
        };
        $scope.today();
        $scope.clear = function() {
            $scope.startDate = null;
            $scope.endDate = null;
            $scope.startArrangementCourseDate = null;
            $scope.endArrangementCourseDate = null;
        };
        $scope.startTime = function() {
            $scope.popup1.opened = true;
        };
        $scope.endTime = function() {
            $scope.popup2.opened = true;
        };
        $scope.startArrangementCourse = function() {
            $scope.popupStartArrangementCourse.opened = true;
        };
        $scope.endArrangementCourse = function() {
            $scope.popupEndArrangementCourse.opened = true;
        };
        $scope.popup1 = {
            opened: false
        };
        $scope.popup2 = {
            opened: false
        };
        $scope.popupStartArrangementCourse = {
            opened: false
        };
        $scope.popupEndArrangementCourse = {
            opened: false
        };

        /**
         * 售课情况
         */
        loadAll();
        function loadAll(){
            BuyCourse.query({},onSuccess,onError);
        }
        function onSuccess(data) {
            $scope.BuyCourses = data;
            console.log($scope.BuyCourses);
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        /**
         * 今日售课
         */
        buyCourseService.loadBuyCourseToday(0,10,function (data,flag) {
            if(!flag){
               // alert(data);
            }
            $scope.buyCoursesToday =data;
            console.log($scope.buyCoursesToday)
        });
        /**
         * 排课情况
         */
        loadSchedulingAll();
        function loadSchedulingAll(){
            CourseScheduling.query({},onScheSuccess,onScheError);
        }
        function onScheSuccess(data) {
            $scope.courseSchedulings = data;
            console.log($scope.courseSchedulings);

        }
        function onScheError(error) {
            AlertService.error(error.data.message);
        }

    }]);

    //今日售课
    app.factory("buyCourseResource",["$resource",function($resource){
        var resourceUrl =  'api/buy-courses/today';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'}
        });

    }]);
    app.service("buyCourseService",["buyCourseResource",function(buyCourseResource){
        this.loadBuyCourseToday=function(index,size,callback){
            buyCourseResource.query({
                index:index,
                size:size
            },function(data){
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
    //获取签到二维码
    app.factory("qrResource",["$resource",function($resource){
        var resourceUrl =  '/api/course-schedulings/qr/scheId';
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
        }

    }]);

})();


