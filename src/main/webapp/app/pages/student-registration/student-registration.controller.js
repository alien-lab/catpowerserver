/**
 * Created by asus on 2017/6/2.
 */
(function () {
    'use strict';
    var app=angular.module('catpowerserverApp');
    app.controller('studentRegistrationController',['$scope','Course','AlertService',function ($scope,Course,AlertService) {

        loadAll();
        function loadAll() {
            Course.query({

            },onSuccess,onError);
            function onSuccess(data) {
                $scope.courses = data;
                console.log($scope.courses);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }]);

})();
