/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('studentFilingController',['$scope','Learner','AlertService',function ($scope,Learner,AlertService) {
        /**
         *获取学员信息
         */
        loadAll();
        function loadAll () {
            Learner.query({}, onSuccess, onError);
            function onSuccess(data) {
                $scope.learners = data;
                console.log($scope.learners);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }]);
})();
