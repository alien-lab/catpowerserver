/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('coachArrangementController',['$scope','coachArrangementResource','CourseScheduling','AlertService',function ($scope,coachArrangementResource,CourseScheduling,AlertService) {

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


        $scope.arrangementlist = coachArrangementResource.loadArrangementList();


        //下拉
        this.openStatus = true;
        $scope.toggleOpen = function (coachId) {
            var index = -1;
            angular.forEach($scope.arrangementlist,function (item,key) {
                if(item.coachId = coachId){
                    index = key;
                }
            });
            if(index != -1){
                this.openStatus = !this.openStatus;
            }
        };
        //分类
        angular.forEach($scope.arrangementlist,function (item) {
            angular.forEach(item.courseCount,function (list) {
                if(list.courseState == '进行中'){

                }
            });
        });
        /*$scope.arrangementings = [];
        $scope.arrangementings1 = [];
        $scope.arrangementings2 = [];
        $scope.arrangementeds = [];
        angular.forEach($scope.arrangementlist,function (item) {
            angular.forEach(item.courseCount,function (list) {
                if(list.courseState == '进行中'){
                    $scope.arrangementings1.push({'coachId':item.coachId,'coachName':item.coachName,'courseCount':list});
                }
            });
        });
        console.log($scope.arrangementings1);

        var personalCourses = [];
        var personalCourses1 = [];
        var personalCourses2 = [];
        for(var i = 0; i < $scope.arrangementings1.length; i++){
            for(var j = 0; j < $scope.arrangementings1.length; j++){
                if( (i != j) && (i > j) && ($scope.arrangementings1[i].coachId == $scope.arrangementings1[j].coachId)){
                    personalCourses1.push($scope.arrangementings1[i].courseCount);
                    personalCourses2.push($scope.arrangementings1[j].courseCount) ;
                    personalCourses = personalCourses1.concat(personalCourses2);
                    $scope.arrangementings2.push({'coachId':$scope.arrangementings1[i].coachId,'coachName':$scope.arrangementings1[i].coachName,'courseCount':personalCourses})
                }
            };
        }
        $scope.arrangementings1.push($scope.arrangementings2);
        console.log($scope.arrangementings1);
        console.log(personalCourses);
        console.log($scope.arrangementings2);
        angular.forEach($scope.arrangementlist,function (item) {
            angular.forEach(item.courseCount,function (list) {
                if(list.courseState == '进行中'){
                    $scope.arrangementings1.push({'coachId':item.coachId,'coachName':item.coachName,'courseCount':$scope.arrangementings});
                }
            });
        });*/


    }]);

    app.service('coachArrangementResource',[function () {
        this.loadArrangementList = function () {
            var arrangements = [{
                coachId:'1502340230',
                coachName:'教练1',
                courseCount:[{
                    coachId:'1502340230',
                    courseId:'20171',
                    courseImg:'http://i01.pictn.sogoucdn.com/d5df0ecae97501f6',
                    courseState:'已结束',
                    courseName:'有氧健身操',
                    startCourseTime:'2016-06-05 17:00',
                    studentNumber:8
                },{
                    coachId:'1502340230',
                    courseId:'20172',
                    courseImg:'http://i01.pictn.sogoucdn.com/8a9843482902ef2f',
                    courseState:'进行中',
                    courseName:'功能训练课程  ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:6
                },{
                    coachId:'1502340230',
                    courseId:'20173',
                    courseImg:'http://i01.pictn.sogoucdn.com/34e1170e14da1a00',
                    courseState:'未开始',
                    courseName:'拳击  ',
                    startCourseTime:'2017-06-15 13:00',
                    studentNumber:6
                }]
            },{
                coachId:'1502340231',
                coachName:'教练2',
                courseCount:[{
                    coachId:'1502340231',
                    courseId:'20174',
                    courseImg:'http://i03.pictn.sogoucdn.com/89f29b46f920a7e8',
                    courseState:'已结束',
                    courseName:'基础实践课程    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:10
                }]
            },{
                coachId:'1502340232',
                coachName:'教练3',
                courseCount:[{
                    coachId:'1502340232',
                    courseId:'20175',
                    courseImg:'http://i02.pictn.sogoucdn.com/0fbbf3268e60bca6',
                    courseState:'进行中',
                    courseName:'普拉提    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                },{
                    coachId:'1502340232',
                    courseId:'20176',
                    courseImg:'http://i03.pictn.sogoucdn.com/cbc17de89c8f511d',
                    courseState:'进行中',
                    courseName:'瑜伽    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                }]
            }];
            return arrangements;
        };

    }]);
})();
