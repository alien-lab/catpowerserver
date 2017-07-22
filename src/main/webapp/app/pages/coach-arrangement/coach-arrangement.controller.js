/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('coachArrangementController',['$scope','CourseScheduling','courseScheService','AlertService','Coach','qrService','ticket',function ($scope,CourseScheduling,courseScheService,AlertService,Coach,qrService,ticket) {

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
        //教练排课表
        courseScheService.loadCoachSche(function (data) {
            $scope.coaches = data;

            console.log($scope.coaches)
            //获取排课签到二维码
            $scope.getscheqr = function (id) {
                console.log(id);
                //console.log(this.sche.id);
                qrService.loadSignQr(id,function (data) {
                    $scope.qr=data;
                    console.log($scope.qr.qrTicker);
                    $scope.scheqr = ticket +$scope.qr.qrTicker;
                    console.log($scope.scheqr);

                });
            };
        });

        /*qrService.loadSignQr(scheId,function (data) {
         $scope.qr=data;
         console.log($scope.qr);
         console.log($scope.qr.qrTicker);
         $scope.arrangementCourseQr = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+$scope.qr.qrTicker;
         console.log($scope.arrangementCourseQr);
         $scope.scheCourseName = scheCourseName;
         });*/
        //定义的下拉的状态
        this.openStatus = true;
        /**
         * 排课情况
         */
        loadCoachAll();
        function loadCoachAll(){
            Coach.query({},onSuccess,onError);
            function onSuccess(data) {
                $scope.coaches = data;
                console.log($scope.coaches);
                //获取教练的排课
                $scope.courseScheList=[];
                angular.forEach($scope.coaches,function (item) {
                    courseScheService.loadCourseSche(item.id,function (data) {
                        $scope.courseSche =data;
                        $scope.courseScheList.push({ 'id':item.id,'coachName':item.coachName, 'courseSche':$scope.courseSche});
                    });
                });
                //下拉
                $scope.toggleOpen = function (coachId) {
                    var index = -1;
                    angular.forEach($scope.coaches,function (item,key) {
                        if(item.id = coachId){
                            index = key;
                        }
                    });
                    if(index != -1){
                        this.openStatus = !this.openStatus;
                    }
                };
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }]);
    app.factory("courseScheResource",["$resource",function($resource){
        var resourceUrl =  'api/course-schedulings/courseScheByCoachId';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',isArray: true},
            'getCoachSche':{url:'api/course-schedulings/courseScheduling',method:'GET',isArray:true}
        });

    }]);
    app.service("courseScheService",["courseScheResource",function(courseScheResource){
        this.loadCourseSche=function(coachId,callback){
            courseScheResource.query({
                coachId:coachId
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
        };
        //获取教练的排课
        this.loadCoachSche = function (callback) {
            courseScheResource.getCoachSche({},function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("shopopResource.query()",error);
                if(callback){
                    callback(error,false);
                }
            });
        }
    }]);

})();

