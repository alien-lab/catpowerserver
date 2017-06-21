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
                    courseImg:'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2847892609,1708440173&fm=26&gp=0.jpg',
                    courseState:'已结束',
                    courseName:'有氧健身操',
                    startCourseTime:'2016-06-05 17:00',
                    studentNumber:8
                },{
                    coachId:'1502340230',
                    courseId:'20172',
                    courseImg:'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3474014184,3118816534&fm=26&gp=0.jpg',
                    courseState:'进行中',
                    courseName:'功能训练课程  ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:6
                },{
                    coachId:'1502340230',
                    courseId:'20173',
                    courseImg:'http://www.chuangtijianshen.com/data/upload/20170110/5874ac0771f8e.jpg',
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
                    courseImg:'http://mpic.tiankong.com/cd2/86b/cd286b8f9867a1afdcf64451706bc01c/640.jpg@360h',
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
                    courseImg:'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=909595617,895328992&fm=26&gp=0.jpg',
                    courseState:'进行中',
                    courseName:'普拉提    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                },{
                    coachId:'1502340232',
                    courseId:'20176',
                    courseImg:'http://preview.quanjing.com/bjisub003/bji02420070.jpg',
                    courseState:'进行中',
                    courseName:'瑜伽    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                }]
            },{
                coachId:'1502340232',
                coachName:'教练4',
                courseCount:[{
                    coachId:'1502340232',
                    courseId:'20175',
                    courseImg:'http://mpic.tiankong.com/ade/07d/ade07d1acb41a12e6c8454195248d547/640.jpg@360h',
                    courseState:'进行中',
                    courseName:'普拉提    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                },{
                    coachId:'1502340232',
                    courseId:'20176',
                    courseImg:'http://mpic.tiankong.com/cd2/86b/cd286b8f9867a1afdcf64451706bc01c/640.jpg@360h',
                    courseState:'进行中',
                    courseName:'瑜伽    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                }]
            },{
                coachId:'1502340232',
                coachName:'教练5',
                courseCount:[{
                    coachId:'1502340232',
                    courseId:'20175',
                    courseImg:'http://mpic.tiankong.com/77f/b86/77fb86de76745fc6d8fb397423cfbe7a/640.jpg@360h',
                    courseState:'进行中',
                    courseName:'普拉提    ',
                    startCourseTime:'2017-06-04 17:00',
                    studentNumber:13
                }]
            }];
            return arrangements;
        };

    }]);
})();
