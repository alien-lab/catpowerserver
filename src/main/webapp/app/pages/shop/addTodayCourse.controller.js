/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';

    var app = angular.module("catpowerserverApp");

    app.controller('addCourseController',['$scope','$uibModalInstance','coachArrangementService',function ($scope,$uibModalInstance,coachArrangementService) {
        $scope.coachArrangements = coachArrangementService.loadCoachArrangement();
        console.log($scope.coachArrangements);
        /*vm.save = save;*/

        var now = new Date();
        var month = now.getMonth()+1;
        var nowDate = now.getFullYear() + '-' + month + '-' + now.getDate() + ' '+now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds() ;
        var coachName1 = '';
        var courseName = '';
        var courseState = '上课中';
        var time = nowDate;
        var traineeCount = '';
        var traineeName = '';
        $scope.coachName1 = $scope.coachName;
        $scope.courseName = courseName;
        $scope.courseState = courseState;
        $scope.time = time;
        $scope.traineeCount = traineeCount;
        $scope.traineeName = traineeName;
        //添加今日课时
        console.log($scope.coachName1);
        console.log($scope.courseName);
        console.log($scope.courseState);
        console.log($scope.time);
        console.log($scope.traineeCount);
        console.log($scope.traineeName);
        $scope.clear = function  () {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.coachArrang = [];
        $scope.save = function () {
            $scope.coachArrangements.push({'coachName':coachName1,'courseName':courseName,'courseState':courseState,'time':time,'traineeCount':traineeCount,'traineeName':traineeName});
            console.log($scope.coachArrangements);
            return $scope.coachArrangements;
        };
    }]);
})();
