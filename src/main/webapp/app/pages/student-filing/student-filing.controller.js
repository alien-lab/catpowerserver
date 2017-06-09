/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('studentFilingController',['$scope','studentsService',function ($scope,studentsService) {
        $scope.students = studentsService.loadStudents();
    }]);

    app.service('studentsService',[function () {
        this.loadStudents = function () {
            var students = [{
                studentId:'1',
                stuName:'陈乐乐',
                stuSex:'男',
                stuPhone:'18851191110',
                registrationTime:'2017-06-06',
                firstGoShopTime:'2017-06-08',
                totalEmpiricalValue:80,
                personalCourse:[{courseId:'111',course:'健身健美'},{courseId:'222',course:'普拉提'}],
                signIn:[{courseId:'111',signState:'已签到'},{courseId:'222',signState:'未签到'}],
                atTheTimeState:[{courseId:'111',atTheTime:'44'},{courseId:'222',atTheTime:'58'}],
                coachName:[{courseId:'111',name:'鞠董'},{courseId:'222',signState:'教练1'}],
                empiricalValue:[{courseId:'111',vaule:30},{courseId:'222',vaule:50}]
            },{
                studentId:'1',
                stuName:'陆丹',
                stuSex:'女',
                stuPhone:'18851191110',
                registrationTime:'2017-06-06',
                firstGoShopTime:'2017-06-08',
                totalEmpiricalValue:80,
                personalCourse:[{courseId:'333',course:'团体操'},{courseId:'222',course:'普拉提'}],
                signIn:[{courseId:'333',signState:'已签到'},{courseId:'222',signState:'未签到'}],
                atTheTimeState:[{courseId:'333',atTheTime:'30'},{courseId:'222',atTheTime:'42'}],
                coachName:[{courseId:'333',name:'鞠董'},{courseId:'222',signState:'教练1'}],
                empiricalValue:[{courseId:'333',vaule:30},{courseId:'222',vaule:50}]
            },{
                studentId:'1',
                stuName:'张三',
                stuSex:'男',
                stuPhone:'18851191110',
                registrationTime:'2017-06-06',
                firstGoShopTime:'2017-06-08',
                totalEmpiricalValue:80,
                personalCourse:[{courseId:'444',course:'团体操'}],
                signIn:[{courseId:'444',signState:'未'}],
                atTheTimeState:[{courseId:'444',atTheTime:'20'}],
                coachName:[{courseId:'444',name:'鞠董'}],
                empiricalValue:[{courseId:'444',vaule:30}]
            }];
            return students;
        }
    }]);
})();
